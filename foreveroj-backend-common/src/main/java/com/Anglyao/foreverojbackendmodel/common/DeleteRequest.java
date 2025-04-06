package com.Anglyao.foreverojbackendmodel.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author <a href="https://github.com/RicardoLou">RicardoLou</a>
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}