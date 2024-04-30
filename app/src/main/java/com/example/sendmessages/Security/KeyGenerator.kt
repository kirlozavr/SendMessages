package com.example.sendmessages.Security

import android.util.Log
import java.math.BigInteger
import java.security.SecureRandom
import java.util.Random

class KeyGenerator {

    private fun generateLargePrime(): BigInteger {
        // Генерация большого простого числа
        while (true) {
            val p = BigInteger(512, Random())
            if (p.isProbablePrime(10)) {
                return p
            }
        }
    }

    private fun gcd(a: BigInteger, b: BigInteger): BigInteger {
        // Нахождение наибольшего общего делителя
        return if (b == BigInteger.ZERO) a else gcd(b, a % b)
    }

    private fun extendedGcd(a: BigInteger, b: BigInteger): Triple<BigInteger, BigInteger, BigInteger> {
        // Расширенный алгоритм Евклида
        return if (a == BigInteger.ZERO) Triple(b, BigInteger.ZERO, BigInteger.ONE)
        else {
            val (gcd, x1, y1) = extendedGcd(b % a, a)
            val x = y1 - (b / a) * x1
            val y = x1
            Triple(gcd, x, y)
        }
    }

    /**
     * Метод генерирует ключи
     */
    fun generateKeypair(): Pair<PublicKeys, PrivateKeys> {
        // Генерация публичного и приватного ключей
        val p = generateLargePrime()
        val q = generateLargePrime()
        val n = p * q
        val phi = (p - BigInteger.ONE) * (q - BigInteger.ONE)
        var e: BigInteger
        while (true) {
            e = BigInteger(phi.bitLength(), Random())
            if (e > BigInteger.valueOf(2) && e < phi && gcd(e, phi) == BigInteger.ONE) {
                break
            }
        }
        val (_, d, _) = extendedGcd(e, phi)
        val privateKey = (d % phi + phi) % phi
        return Pair(PublicKeys(e, n), PrivateKeys(privateKey, n))
    }

    /**
     * Метод генерирует стабильный ключ исходя из входных данных
     */
    fun generateKeypair(id: Long, username: String): Pair<PublicKeys, PrivateKeys> {
        // Генерация публичного и приватного ключей

        var count = 0L

        var p: BigInteger
        while (true){
            val preP = getNumberFromTwoLogins(username.repeat(8) + id, "FNJ#J!N2*SFN#N".repeat(6)) + count
            p = BigInteger(512, Random(preP))
            count += 1
            if (p.isProbablePrime(10)) {
                break
            }
        }

        count = 0L
        var q: BigInteger
        while (true){
            val preQ = getNumberFromTwoLogins(username.repeat(24) + id, "=BSFUIA(#)$)(%@#DJNJ".repeat(7) ) + count
            q = BigInteger(512, Random(preQ))
            count += 1
            if (q.isProbablePrime(10)) {
                break
            }
        }

        val n = p * q
        val phi = (p - BigInteger.ONE) * (q - BigInteger.ONE)

        count = 0
        var e: BigInteger
        while (true){
            val preE = getNumberFromTwoLogins(username.repeat(36) + id + phi.toByteArray(), "UH6WFUNX_$@XRMJ#@".repeat(3) ) + count
            e = BigInteger(512, Random(preE))
            count += 1
            if (e > BigInteger.valueOf(2) && e < phi && gcd(e, phi) == BigInteger.ONE) {
                break
            }
        }

        val (_, d, _) = extendedGcd(e, phi)
        val privateKey = (d % phi + phi) % phi
        return Pair(PublicKeys(e, n), PrivateKeys(privateKey, n))
    }

    /**
     * Метод зашифровывает сообщение
     */
    fun encrypt(message: String, publicKey: PublicKeys): List<BigInteger> {
        // Шифрование сообщения с использованием публичного ключа
        return message.map {
            BigInteger.valueOf(it.toLong()).modPow(publicKey.firstPublicKey, publicKey.secondPublicKey)
        }
    }

    /**
     * Метод расшифровывает сообщение
     */
    fun decrypt(encryptedMessage: List<BigInteger>, privateKey: PrivateKeys): String {
        // Расшифровка сообщения с использованием приватного ключа
        val decryptedMsg = encryptedMessage.map {
            Char(it.modPow(privateKey.firstPrivateKey, privateKey.secondPrivateKey).toInt())
        }
        return decryptedMsg.joinToString("")
    }

    /**
     * Дополнительно шифрует ключи
     */
    fun encryptKeys(publicKey: PublicKeys, privateKey: PrivateKeys): Keys {
        val keyConversion = KeyConversion(
            publicFirstKey = kotlin.random.Random.nextLong(32768, 262144),
            publicSecondKey = kotlin.random.Random.nextLong(32768, 262144),
            privateFirstKey = kotlin.random.Random.nextLong(32768, 262144),
            privateSecondKey = kotlin.random.Random.nextLong(32768, 262144)
        )
        val newPublicKeys = PublicKeys(
            firstPublicKey = publicKey.firstPublicKey.multiply(BigInteger.valueOf(keyConversion.publicFirstKey)),
            secondPublicKey = publicKey.secondPublicKey.multiply(BigInteger.valueOf(keyConversion.publicSecondKey))
        )
        val newPrivateKeys = PrivateKeys(
            firstPrivateKey = privateKey.firstPrivateKey.multiply(BigInteger.valueOf(keyConversion.privateFirstKey)),
            secondPrivateKey = privateKey.secondPrivateKey.multiply(BigInteger.valueOf(keyConversion.privateSecondKey))
        )

        return Keys(
            keyConversion = keyConversion,
            publicKeys = newPublicKeys,
            privateKeys = newPrivateKeys
        )
    }

    /**
     * Расшифровывает ключи
     */
    fun decryptKeys(keyConversion: KeyConversion, publicKey: PublicKeys, privateKey: PrivateKeys): Pair<PublicKeys, PrivateKeys> {
        val newPublicKeys = PublicKeys(
            firstPublicKey = publicKey.firstPublicKey.divide(BigInteger.valueOf(keyConversion.publicFirstKey)),
            secondPublicKey = publicKey.secondPublicKey.divide(BigInteger.valueOf(keyConversion.publicSecondKey))
        )
        val newPrivateKeys = PrivateKeys(
            firstPrivateKey = privateKey.firstPrivateKey.divide(BigInteger.valueOf(keyConversion.privateFirstKey)),
            secondPrivateKey = privateKey.secondPrivateKey.divide(BigInteger.valueOf(keyConversion.privateSecondKey))
        )

        return Pair(newPublicKeys, newPrivateKeys)
    }

    /**
     * Метод дополнительно шифрует все ключи
     */
    fun encryptKeysForDataBase(keys: Keys, number: Long): Keys{
        return Keys(
            publicKeys = PublicKeys(keys.publicKeys.firstPublicKey.multiply(BigInteger.valueOf(number)), keys.publicKeys.secondPublicKey.multiply(BigInteger.valueOf(number))),
            privateKeys = PrivateKeys(keys.privateKeys.firstPrivateKey.multiply(BigInteger.valueOf(number)), keys.privateKeys.secondPrivateKey.multiply(BigInteger.valueOf(number))),
            keyConversion = KeyConversion(
                publicFirstKey = keys.keyConversion.publicFirstKey * number,
                publicSecondKey = keys.keyConversion.publicSecondKey * number,
                privateFirstKey = keys.keyConversion.privateFirstKey * number,
                privateSecondKey = keys.keyConversion.privateSecondKey * number
            )
        )
    }

    /**
     * Метод расшифровывает все ключи
     */
    fun decryptKeysForDataBase(keys: Keys, number: Long): Keys{
        return Keys(
            publicKeys = PublicKeys(keys.publicKeys.firstPublicKey.divide(BigInteger.valueOf(number)), keys.publicKeys.secondPublicKey.divide(BigInteger.valueOf(number))),
            privateKeys = PrivateKeys(keys.privateKeys.firstPrivateKey.divide(BigInteger.valueOf(number)), keys.privateKeys.secondPrivateKey.divide(BigInteger.valueOf(number))),
            keyConversion = KeyConversion(
                publicFirstKey = keys.keyConversion.publicFirstKey / number,
                publicSecondKey = keys.keyConversion.publicSecondKey / number,
                privateFirstKey = keys.keyConversion.privateFirstKey / number,
                privateSecondKey = keys.keyConversion.privateSecondKey / number
            )
        )
    }

    /**
     * Метод разбивает логины на массив символов и складывает их
     */
    fun getNumberFromTwoLogins(firstName: String, secondName: String): Long{
        return (firstName.toCharArray().sumOf { it.code } + secondName.toCharArray().sumOf { it.code }).toLong()
    }

    /**
     * Метод переводит keys в строку
     */
    fun keysToString(keys: Keys): String{
        return keys.publicKeys.firstPublicKey.toString() + "|" +
            keys.publicKeys.secondPublicKey.toString() + "|" +
            keys.privateKeys.firstPrivateKey.toString() + "|" +
            keys.privateKeys.secondPrivateKey.toString() + "|" +
            keys.keyConversion.publicFirstKey.toString() + "|" +
            keys.keyConversion.publicSecondKey.toString() + "|" +
            keys.keyConversion.privateFirstKey.toString() + "|" +
            keys.keyConversion.privateSecondKey.toString()
    }

    /**
     * Метод переводит строку в keys
     */
    fun stringToKeys(stringKeys: String): Keys{
        val listKeys = stringKeys.split("|")
        return Keys(
            publicKeys = PublicKeys(listKeys[0].toBigInteger(), listKeys[1].toBigInteger()),
            privateKeys = PrivateKeys(listKeys[2].toBigInteger(), listKeys[3].toBigInteger()),
            keyConversion = KeyConversion(listKeys[4].toLong(), listKeys[5].toLong(), listKeys[6].toLong(), listKeys[7].toLong())
        )
    }

    /**
     * Список зашифрованных значений в строку
     */
    fun listBigIntegerToString(list: List<BigInteger>): String{
        return list.joinToString("|")
    }

    /**
     * Список зашифрованных значений пароля в строку
     */
    fun listBigIntegerPasswordToString(list: List<BigInteger>): String{
        return list.joinToString("")
    }

    /**
     * Строку в список зашифрованных значений
     */
    fun stringToListBigInteger(value: String): List<BigInteger>{
        return value.split("|").map { it.toBigInteger() }
    }
}