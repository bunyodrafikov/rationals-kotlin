package bunyodrafikov.rationals

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO


class Rational(n: BigInteger, d: BigInteger): Comparable<Rational> {
    val numer: BigInteger
    val denom: BigInteger

    init {
        val g = n.gcd(d)
        val sign = d.signum().toBigInteger()
        numer = n / g * sign
        denom = d / g * sign
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (numer != other.numer) return false
        if (denom != other.denom) return false

        return true
    }
    override fun hashCode(): Int {
        var result = numer.hashCode()
        result = 31 * result + denom.hashCode()
        return result
    }
    override fun toString(): String {
        return when {
            denom == 1.toBigInteger() || numer.rem(denom) == 0.toBigInteger() -> numer.div(denom).toString()
            else -> {
                val r = simplify(this)

                if(r.denom < 0.toBigInteger() || (r.numer < 0.toBigInteger() && r.denom < 0.toBigInteger())){
                    formatRational(Rational(r.numer.negate(), r.denom.negate()))
                } else {
                    formatRational(Rational(r.numer, r.denom))
                }
            }
        }
    }

    fun formatRational(r: Rational): String = r.numer.toString() + "/" + r.denom.toString()

    operator fun unaryMinus():Rational = Rational(-numer, denom)
    operator fun plus(other:Rational):Rational = Rational(
        numer * other.denom + other.numer * denom,
        denom * other.denom)
    operator fun minus(other:Rational):Rational = Rational(
        numer * other.denom - other.numer * denom,
        denom * other.denom)
    operator fun times(other:Rational):Rational = Rational(numer * other.numer, denom * other.denom)
    operator fun div(other:Rational):Rational = Rational(numer * other.denom, denom * other.numer)

    override fun compareTo(other: Rational): Int {
        return (numer * other.denom - denom * other.numer).signum()
    }
}
fun hcf(n1: BigInteger, n2: BigInteger): BigInteger =
    if (n2 != 0.toBigInteger()) hcf(n2, n1 % n2) else n1

fun simplify(r1: Rational): Rational {
    val hcf = hcf(r1.numer, r1.denom).abs()
    return Rational(r1.numer.div(hcf), r1.denom.div(hcf))
}

fun String.toRational(): Rational {
    val number = split("/")

    when {
        number.size == 1 -> return Rational(number[0].toBigInteger(), 1.toBigInteger())
        else -> return Rational(number[0].toBigInteger(), number[1].toBigInteger())
    }
}

infix fun Int.divBy(denom: Int) = Rational(this.toBigInteger(), denom.toBigInteger())
infix fun Long.divBy(denom: Long) = Rational(this.toBigInteger(), denom.toBigInteger())
infix fun BigInteger.divBy(denom: BigInteger) = Rational(this, denom)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}