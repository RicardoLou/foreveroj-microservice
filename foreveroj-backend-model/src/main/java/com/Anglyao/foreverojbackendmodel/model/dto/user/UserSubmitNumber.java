package com.Anglyao.foreverojbackendmodel.model.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSubmitNumber {
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * id
     */
    private Long id;

    /**
     * 提交数
     */
    private Integer submitNumber;

    /**
     * 通过数
     */
    private Integer acceptNumber;
}
