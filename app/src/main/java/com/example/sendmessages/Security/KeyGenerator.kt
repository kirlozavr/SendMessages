package com.example.sendmessages.Security

import java.math.BigInteger
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

    fun encrypt(message: String, publicKey: PublicKeys): List<BigInteger> {
        // Шифрование сообщения с использованием публичного ключа
        return message.map {
            BigInteger.valueOf(it.toLong()).modPow(publicKey.firstPublicKey, publicKey.secondPublicKey)
        }
    }

    fun decrypt(encryptedMessage: List<BigInteger>, privateKey: PrivateKeys): String {
        // Расшифровка сообщения с использованием приватного ключа
        val decryptedMsg = encryptedMessage.map {
            Char(it.modPow(privateKey.firstPrivateKey, privateKey.secondPrivateKey).toInt())
        }
        return decryptedMsg.joinToString("")
    }
}