package com.taufik.ceritaku.utils.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object Common {
    fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun formattedDate(currentDateString: String, targetZone: String): String {
        val instant = Instant.parse(currentDateString)
        val outputFormatter = DateTimeFormatter.ofPattern(CommonConstant.DATE_DD_MMMM_YYYY_FULL_FORMAT)
            .withZone(ZoneId.of(targetZone))
        return outputFormatter.format(instant)
    }
}