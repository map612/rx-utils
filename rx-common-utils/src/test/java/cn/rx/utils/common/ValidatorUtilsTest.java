package cn.rx.utils.common;

/**
 * ValidatorUtils Test
 * <p/>
 * 创建时间: 16/11/17<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class ValidatorUtilsTest {
    public static void main(String[] args) {
        System.out.println(String.format("ValidatorUtils.isInt(\"98.2\"): \t%s", ValidatorUtils.isInt("98.2")));
        System.out.println(String.format("ValidatorUtils.isIntNeg(\"-12\"): \t%s", ValidatorUtils.isIntNeg("-12")));
        System.out.println(String.format("ValidatorUtils.isIntPos(\"12\"): \t%s", ValidatorUtils.isIntPos("12")));

        System.out.println(String.format("ValidatorUtils.isNum(\"98.29\"): \t%s", ValidatorUtils.isNum("98.29")));
        System.out.println(String.format("ValidatorUtils.isNumNeg(\"-98.29\"): \t%s", ValidatorUtils.isNumNeg("-98.29")));
        System.out.println(String.format("ValidatorUtils.isNumPos(\"98.29\"): \t%s", ValidatorUtils.isNumPos("98.29")));

        System.out.println(String.format("ValidatorUtils.isDecimal(\"98.29\"): \t%s", ValidatorUtils.isDecimal("98.29")));
        System.out.println(String.format("ValidatorUtils.isDecimalNeg(\"-98.29\"): \t%s", ValidatorUtils.isDecimalNeg("-98.29")));
        System.out.println(String.format("ValidatorUtils.isDecimalPos(\"98.29\"): \t%s", ValidatorUtils.isDecimalPos("98.29")));

        System.out.println(String.format("ValidatorUtils.isEmail(\"map612@163.com\"): \t%s", ValidatorUtils.isEmail("map612@163.com")));
        System.out.println(String.format("ValidatorUtils.isColor(\"#998877\"): \t%s", ValidatorUtils.isColor("#FFF")));
//        System.out.println(String.format("ValidatorUtils.isUrl(\"http://www.baidu.com\"): \t%s", ValidatorUtils.isUrl("http://www.baidu.com")));
        System.out.println(String.format("ValidatorUtils.isChinese(\"真屌啊aa\"): \t%s", ValidatorUtils.isChinese("真屌啊")));
        System.out.println(String.format("ValidatorUtils.isAscii(\"asdf\"): \t%s", ValidatorUtils.isAscii("asdf")));

        System.out.println(String.format("ValidatorUtils.isZipcode(\"710200\"): \t%s", ValidatorUtils.isZipcode("710200")));
        System.out.println(String.format("ValidatorUtils.isMobile(\"13436927075\"): \t%s", ValidatorUtils.isMobile("13436927075")));
        System.out.println(String.format("ValidatorUtils.isIpV4(\"255.255.255.255\"): \t%s", ValidatorUtils.isIpV4("255.255.255.255")));
        System.out.println(String.format("ValidatorUtils.isNotEmpty(\"asdf\"): \t%s", ValidatorUtils.isNotEmpty("asdf")));
        System.out.println(String.format("ValidatorUtils.isPicture(\".jpg\"): \t%s", ValidatorUtils.isPicture(".jpg")));
        System.out.println(String.format("ValidatorUtils.isRar(\".zip\"): \t%s", ValidatorUtils.isRar(".zip")));
        System.out.println(String.format("ValidatorUtils.isDate(\"2015-09-19\", DateUtil.getDatePattern()): \t%s", ValidatorUtils.isDate("2015-09-19", DateUtils.getDatePattern())));
        System.out.println(String.format("ValidatorUtils.isQQ(\"102000\"): \t%s", ValidatorUtils.isQQ("102000")));
        System.out.println(String.format("ValidatorUtils.isTel(\"029-86011771\"): \t%s", ValidatorUtils.isTel("029-86011771")));
        System.out.println(String.format("ValidatorUtils.isLetter(\"asdf\"): \t%s", ValidatorUtils.isLetter("asdf")));
        System.out.println(String.format("ValidatorUtils.isLetterUpper(\"ASDF\"): \t%s", ValidatorUtils.isLetterUpper("ASDF")));
        System.out.println(String.format("ValidatorUtils.isLetterLower(\"asdf\"): \t%s", ValidatorUtils.isLetterLower("asdf")));
    }
}
