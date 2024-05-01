package com.example.sendmessages.Service

import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import com.example.sendmessages.Adapters.RecyclerViewAdapterMessages
import com.example.sendmessages.Common.DataBase
import com.example.sendmessages.Common.DateFormat
import com.example.sendmessages.DTO.ChatsDto
import com.example.sendmessages.DTO.MessageDto
import com.example.sendmessages.Entity.ChatsEntity
import com.example.sendmessages.Entity.MessageEntity
import com.example.sendmessages.Interface.Mapping
import com.example.sendmessages.Mapping.ChatsMapper
import com.example.sendmessages.Mapping.MessageMapper
import com.example.sendmessages.Security.KeyGenerator
import com.example.sendmessages.Security.Keys
import com.example.sendmessages.Security.PrivateKeys
import com.example.sendmessages.Security.PublicKeys
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Класс отвечает за операции с БД,
 * в частности прием/отправка сообщений пользователю с которым ведется переписка.
 */
class MessageService() {
    private val db: FirebaseFirestore
    var adapterMessages: RecyclerViewAdapterMessages? = null
        private set
    var uriImage: Uri? = null
        private set
    var imageView: ImageView? = null
        private set
    var editText: EditText? = null
        private set
    var usernameFrom: String? = null
        private set
    var usernameToWhom: String? = null
        private set
    private val messageMapper: MessageMapper = MessageMapper()
    private val chatsMapper: ChatsMapper = ChatsMapper()
    private var messageDto: MessageDto? = null
    private var chatsDtoFrom: ChatsDto? = null
    private var chatsDtoToWhom: ChatsDto? = null
    private val keyGenerator = KeyGenerator()
    private var pairKeys: Pair<PublicKeys, PrivateKeys>? = null
    private var number: Long = 0
    private var boolChatExist = false

    init {
        db = FirebaseFirestore.getInstance()
    }

    fun setAdapterMessages(adapterMessages: RecyclerViewAdapterMessages?): MessageService {
        this.adapterMessages = adapterMessages
        return this
    }

    fun setImageView(imageView: ImageView?): MessageService {
        this.imageView = imageView
        return this
    }

    fun setEditText(editText: EditText?): MessageService {
        this.editText = editText
        return this
    }

    fun setUsernameFrom(usernameFrom: String?): MessageService {
        this.usernameFrom = usernameFrom
        return this
    }

    fun setUsernameToWhom(usernameToWhom: String?): MessageService {
        this.usernameToWhom = usernameToWhom
        return this
    }

    fun setUriImage(uriImage: Uri?): MessageService {
        this.uriImage = uriImage
        return this
    }

    fun deleteUriImage() {
        uriImage = null
    }

    /**
     * Метод отвечает за создание сущности сообщения
     */
    private fun setMessagesEntity(): Boolean {
        /** Проверка на наличие текста в текстовом поле  */
        val isExistText = (editText!!.text.toString().trim { it <= ' ' } != ""
            && editText!!.text.toString().trim { it <= ' ' }.length != 0)
        if ((isExistText
                || uriImage != null)) {
            messageDto = MessageDto(
                editText!!.text.toString().trim { it <= ' ' },
                DateFormat.getFormatToDataBase().format(ZonedDateTime.now()),
                (usernameFrom)!!,
                null
            )
            if (uriImage != null) {
                messageDto!!.uriImage = uriImage.toString()
            }
            deleteUriImage()
            return true
        } else {
            return false
        }
    }

    /**
     * Метод отвечает за создание сущности чата, создается 2 сущности,
     * чтобы создать 2 одинаковые записи в бд с одинаковым id.
     * Чат А с В по id:1 и чат В с А по id:1
     */
    private fun setChatsEntity() {
        chatsDtoFrom = ChatsDto(
            usernameToWhom = usernameToWhom!!
        )
        chatsDtoToWhom = ChatsDto(
            chatsDtoFrom!!.idChats,
            (usernameFrom)!!
        )
    }

    /**
     * Метод отвечает за добавление чатов в БД
     */
    private fun addChatsToDataBase() {
        number = keyGenerator.getNumberFromTwoLogins((usernameFrom)!!, (usernameToWhom)!!)
        pairKeys = keyGenerator.generateKeypair()
        val keys = keyGenerator.encryptKeys(pairKeys!!.first, pairKeys!!.second)
        val keysForDataBase = keyGenerator.encryptKeysForDataBase(keys, number)
        val lineKeys = keyGenerator.keysToString(keysForDataBase)

        chatsDtoFrom = ChatsDto(
            chatsDtoFrom!!.idChats,
            chatsDtoFrom!!.usernameToWhom,
            messageDto!!.message,
            messageDto!!.dateTimeToDataBase,
            lineKeys
        )

        chatsDtoToWhom = ChatsDto(
            chatsDtoToWhom!!.idChats,
            chatsDtoToWhom!!.usernameToWhom,
            messageDto!!.message,
            messageDto!!.dateTimeToDataBase,
            lineKeys
        )

        db
            .collection(DataBase.CHATS_DB)
            .document((usernameFrom)!!)
            .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
            .document((usernameToWhom)!!)
            .set(chatsMapper.getDtoToEntity(chatsDtoFrom, pairKeys))

        db
            .collection(DataBase.CHATS_DB)
            .document((usernameToWhom)!!)
            .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
            .document((usernameFrom)!!)
            .set(chatsMapper.getDtoToEntity(chatsDtoToWhom, pairKeys))

        boolChatExist = false
    }

    fun getChat() {
        /**
         * Метод отвечает за получение чата с конкретным пользователем из БД,
         * если его еще нет, то создается новый.
         * Так же выводит все сообщения между пользователями.
         */
        db
            .collection(DataBase.CHATS_DB)
            .document((usernameFrom)!!)
            .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
            .document((usernameToWhom)!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val chatsEntityFrom = documentSnapshot
                        .toObject(ChatsEntity::class.java)

                    val keys: Keys = keyGenerator.stringToKeys(chatsEntityFrom!!.lineKeys)
                    val number = keyGenerator.getNumberFromTwoLogins(usernameFrom!!, chatsEntityFrom.usernameToWhom)
                    val (keyConversion, publicKeys, privateKeys) = keyGenerator.decryptKeysForDataBase(keys, number)
                    pairKeys = keyGenerator.decryptKeys(keyConversion, publicKeys, privateKeys)

                    chatsDtoFrom = chatsEntityFrom.let { chatsMapper.getEntityToDto(it, pairKeys) }
                    boolChatExist = true
                } else {
                    setChatsEntity()
                }
                messagesFromDataBase
            }
    }

    /**
     * Метод отвечает за обновление чата в БД, происходит перезапись значений:
     * Последнее сообщение, время отправки сообщения.
     */
    private fun updateChats() {
        val message = keyGenerator.listBigIntegerToString(
            keyGenerator.encrypt(messageDto!!.message, pairKeys!!.first)
        )
        val dateTimeToDataBase = keyGenerator.listBigIntegerToString(
            keyGenerator.encrypt(messageDto!!.dateTimeToDataBase, pairKeys!!.first)
        )


        db
            .collection(DataBase.CHATS_DB)
            .document((usernameFrom)!!)
            .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
            .document((usernameToWhom)!!)
            .update(
                "lastMessage", message,
                "timeMessage", dateTimeToDataBase
            )
        db
            .collection(DataBase.CHATS_DB)
            .document((usernameToWhom)!!)
            .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
            .document((usernameFrom)!!)
            .update(
                "lastMessage", message,
                "timeMessage", dateTimeToDataBase
            )
        boolChatExist = true
    }

    /**
     * Метод отвечает за запись сообщения в БД.
     */
    fun addMessagesToDataBase() {
        val booleanMess = setMessagesEntity()
        if (booleanMess) {
            if (!boolChatExist) {
                addChatsToDataBase()
            }
            db
                .collection(DataBase.MESSAGES_DB)
                .document(chatsDtoFrom!!.idChats.toString())
                .collection(DataBase.ListTag.COLLECTIONS_MESSAGES_TAG)
                .document(Instant.now().epochSecond.toString())
                .set(messageMapper.getDtoToEntity(messageDto, pairKeys))

            editText?.text?.clear()
            updateChats()
        }
    }

    private val messagesFromDataBase: Unit
        /**
         * Метод отвечает за получение списка всех сообщений с пользователем с которым ведется переписка из БД.
         */
        private get() {
            val messageList: MutableList<MessageDto> = ArrayList()
            try {
                db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsDtoFrom!!.idChats.toString())
                    .collection(DataBase.ListTag.COLLECTIONS_MESSAGES_TAG)
                    .addSnapshotListener(
                        EventListener { value, error ->
                            val localDate = LocalDate.now()
                            adapterMessages!!.deleteList()
                            for (ds: DocumentSnapshot in value!!.documents) {
                                /**
                                 * Проверка на существование даты, если ее нет,
                                 * то конвертация через маппер не осуществляется.
                                 */
                                if (
                                    ds.toObject<MessageEntity>(MessageEntity::class.java)
                                        ?.timeMessage != null
                                ) {

                                    val messageEntity = ds.toObject(MessageEntity::class.java)!!

                                    val zonedDateTime = ZonedDateTime.parse(
                                        keyGenerator.decrypt(
                                            keyGenerator.stringToListBigInteger(messageEntity.timeMessage),
                                            pairKeys!!.second
                                        ),
                                        DateFormat.getFormatFromDataBase()
                                    )
                                    /**
                                     * Проверка на сегодняшнюю дату,
                                     * если дата отправки сообщения совпадает с сегодняшней,
                                     * то выводится только время, если нет,
                                     * то дополнительно выводится год, месяц и день.
                                     */
                                    if (zonedDateTime.toLocalDate().isEqual(localDate)) {
                                        messageMapper.setIsToday(false)
                                    } else {
                                        messageMapper.setIsToday(true)
                                    }
                                    val message = messageMapper
                                        .getEntityToDto(messageEntity, pairKeys)
                                    messageList.add(message)
                                }
                            }
                            adapterMessages!!.setList(messageList)
                        })
            } catch (e: Exception) {
            }
        }
}