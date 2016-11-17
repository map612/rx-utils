package cn.rx.common;

/**
 * ValidatorUtil Test
 * <p/>
 * 创建时间: 16/11/17<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class ValidatorUtilTest {
    public static void main(String[] args) {
        System.out.println(String.format("ValidatorUtil.isInt(\"98.2\"): \t%s", ValidatorUtil.isInt("98.2")));
        System.out.println(String.format("ValidatorUtil.isIntNeg(\"-12\"): \t%s", ValidatorUtil.isIntNeg("-12")));
        System.out.println(String.format("ValidatorUtil.isIntPos(\"12\"): \t%s", ValidatorUtil.isIntPos("12")));

        System.out.println(String.format("ValidatorUtil.isNum(\"98.29\"): \t%s", ValidatorUtil.isNum("98.29")));
        System.out.println(String.format("ValidatorUtil.isNumNeg(\"-98.29\"): \t%s", ValidatorUtil.isNumNeg("-98.29")));
        System.out.println(String.format("ValidatorUtil.isNumPos(\"98.29\"): \t%s", ValidatorUtil.isNumPos("98.29")));

        System.out.println(String.format("ValidatorUtil.isDecimal(\"98.29\"): \t%s", ValidatorUtil.isDecimal("98.29")));
        System.out.println(String.format("ValidatorUtil.isDecimalNeg(\"-98.29\"): \t%s", ValidatorUtil.isDecimalNeg("-98.29")));
        System.out.println(String.format("ValidatorUtil.isDecimalPos(\"98.29\"): \t%s", ValidatorUtil.isDecimalPos("98.29")));

        System.out.println(String.format("ValidatorUtil.isEmail(\"map612@163.com\"): \t%s", ValidatorUtil.isEmail("map612@163.com")));
        System.out.println(String.format("ValidatorUtil.isColor(\"#998877\"): \t%s", ValidatorUtil.isColor("#FFF")));
        System.out.println(String.format("ValidatorUtil.isUrl(\"http://www.baidu.com\"): \t%s", ValidatorUtil.isUrl("http://www.baidu.com")));
        System.out.println(String.format("ValidatorUtil.isChinese(\"真屌啊aa\"): \t%s", ValidatorUtil.isChinese("真屌啊")));
        System.out.println(String.format("ValidatorUtil.isAscii(\"asdf\"): \t%s", ValidatorUtil.isAscii("asdf")));

        System.out.println(String.format("ValidatorUtil.isZipcode(\"710200\"): \t%s", ValidatorUtil.isZipcode("710200")));
        System.out.println(String.format("ValidatorUtil.isMobile(\"13436927075\"): \t%s", ValidatorUtil.isMobile("13436927075")));
        System.out.println(String.format("ValidatorUtil.isIpV4(\"255.255.255.255\"): \t%s", ValidatorUtil.isIpV4("255.255.255.255")));
        System.out.println(String.format("ValidatorUtil.isNotEmpty(\"asdf\"): \t%s", ValidatorUtil.isNotEmpty("asdf")));
        System.out.println(String.format("ValidatorUtil.isPicture(\".jpg\"): \t%s", ValidatorUtil.isPicture(".jpg")));
        System.out.println(String.format("ValidatorUtil.isRar(\".zip\"): \t%s", ValidatorUtil.isRar(".zip")));
        System.out.println(String.format("ValidatorUtil.isDate(\"2015-09-19\", DateUtil.getDatePattern()): \t%s", ValidatorUtil.isDate("2015-09-19", DateUtil.getDatePattern())));
        System.out.println(String.format("ValidatorUtil.isQQ(\"102000\"): \t%s", ValidatorUtil.isQQ("102000")));
        System.out.println(String.format("ValidatorUtil.isTel(\"029-86011771\"): \t%s", ValidatorUtil.isTel("029-86011771")));
        System.out.println(String.format("ValidatorUtil.isLetter(\"asdf\"): \t%s", ValidatorUtil.isLetter("asdf")));
        System.out.println(String.format("ValidatorUtil.isLetterUpper(\"ASDF\"): \t%s", ValidatorUtil.isLetterUpper("ASDF")));
        System.out.println(String.format("ValidatorUtil.isLetterLower(\"asdf\"): \t%s", ValidatorUtil.isLetterLower("asdf")));
    }
}
