package eu.chi.luh.projectradiation.mathfunction

import org.json.JSONArray
import org.json.JSONObject


fun <T : Comparable<T>> greaterLesserFun(def: CompareOp): (T, T) -> Boolean {
    val rFun: (T, T) -> Boolean = when (def) {
        CompareOp.LESSER -> { a: T, b: T -> a < b }
        CompareOp.GREATER -> { a: T, b: T -> a > b }
    }
    return rFun
}

/**
 * Finds the extreme values of a given JSONArray with a given key and comparator
 *
 * @exception Exception The exception will be thrown if the defined comparator is not within
 *                      the functions limits.
 * @param data The JSON Array to be searched.
 * @param searchKeyword The key for the json dictionary
 * @param compareValue This is the comparison point which the other values will be compared to
 * @param define defines the comparator CompareOp
 * @return Return the extreme value
 */
fun getExtreme(
    data: JSONArray,
    searchKeyword: String,
    compareValue: Double,
    define: CompareOp
): Double {
    val tmpFun: (Double, Double) -> Boolean = greaterLesserFun(define)

    var extremeAmount: Double = compareValue

    for (hourlyIndex in 0 until data.length()) {
        val dataSet: JSONObject = data.getJSONObject(hourlyIndex)
        val hourlyData = dataSet.get(searchKeyword) as Number
        val hourlyTmp: Double = hourlyData.toDouble()
//        Log.d("MATH FUNC", "$hourlyUvi")
        if (tmpFun(hourlyTmp, extremeAmount)) extremeAmount = hourlyTmp
    }
    return extremeAmount
}

/**
 * Finds the extreme values of a given JSONArray with a given key and comparator
 *
 * @exception Exception The exception will be thrown if the defined comparator is not within
 *                      the functions limits.
 * @param data The JSON Array to be searched.
 * @param searchKeyword The key for the json dictionary
 * @param compareValue This is the comparison point which the other values will be compared to
 * @param define defines the comparator CompareOp
 * @return Return the extreme value
 */
fun getExtreme(
    data: JSONArray,
    searchKeyword: String,
    compareValue: Number,
    define: CompareOp
): Double {
    return getExtreme(data, searchKeyword, compareValue.toDouble(), define)
}

fun getAverage(data: JSONArray, searchKeyword: String): Double {
    var sum = 0.0

    val amountVal = data.length()
    for (hourlyIndex in 0 until amountVal) {
        val dataSet: JSONObject = data.getJSONObject(hourlyIndex)
        val hourlyData = dataSet.get(searchKeyword) as Number
        sum += hourlyData.toDouble()
    }

    return sum / amountVal
}