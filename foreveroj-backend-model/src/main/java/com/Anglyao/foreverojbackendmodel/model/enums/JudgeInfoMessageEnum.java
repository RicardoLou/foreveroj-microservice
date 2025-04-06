package com.Anglyao.foreverojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传业务类型枚举
 *
 * @author <a href="https://github.com/RicardoLou">RicardoLou</a>
 */
public enum JudgeInfoMessageEnum {

    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "WA"),
    COMPILE_ERROR("编译错误", "CE"),
    MEMORY_LIMIT_EXCEEDED("超出内存限制", "MLE"),
    TIME_LIMIT_EXCEEDED("超出时间限制", "TLE"),
    PRESENTATION_ERROR("格式错误", "PE"),
    PENDING("等待中", "Pending"),
    OUTPUT_LIMIT("输出溢出", "Output Limit"),
    DANGEROUS_OPERATION("危险操作", "Dangerous Operation"),
    SYSTEM_ERROR("系统错误", "System Error");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
