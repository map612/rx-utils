package cn.rx.common.file;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties文件读取
 */
public class PropertyLoadUtils {
    private static final Logger log = LoggerFactory.getLogger(PropertyLoadUtils.class);

    /**
     * 涉及到支持 junit测试   文件路径会自动  进行  /PP/PP  和 PP/PP的匹配
     *
     * @param file  文件路径，传入需要带 /前缀
     * @return
     */
    public static Properties getFile(String file){
        Properties properties = new Properties();

        try {
            if(!StringUtils.isNotBlank(file)) {//没有配置 从classloader获取
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
                if(is == null){
                    file = file.substring(1);
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
                }
                properties.load(is);
            }
        } catch (IOException e) {
            log.error("load properties {} error", file, e);
        }

        return properties;
    }
}
