package com.Anglyao.foreverojbackendmodel.model.dto.question;

import com.Anglyao.foreverojbackendmodel.model.entity.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子 ES 包装类
 *
 * @author <a href="https://github.com/RicardoLou">RicardoLou</a>
 **/
// todo 取消注释开启 ES（须先配置 ES）
//@Document(indexName = "post")
@Data
public class QuestionEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    private static final Gson GSON = new Gson();

    /**
     * 对象转包装类
     *
     * @param post
     * @return
     */
    public static QuestionEsDTO objToDto(Question post) {
        if (post == null) {
            return null;
        }
        QuestionEsDTO postEsDTO = new QuestionEsDTO();
        BeanUtils.copyProperties(post, postEsDTO);
        String tagsStr = post.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            postEsDTO.setTags(GSON.fromJson(tagsStr, new TypeToken<List<String>>() {
            }.getType()));
        }
        return postEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param postEsDTO
     * @return
     */
    public static Question dtoToObj(QuestionEsDTO postEsDTO) {
        if (postEsDTO == null) {
            return null;
        }
        Question post = new Question();
        BeanUtils.copyProperties(postEsDTO, post);
        List<String> tagList = postEsDTO.getTags();
        if (CollectionUtils.isNotEmpty(tagList)) {
            post.setTags(GSON.toJson(tagList));
        }
        return post;
    }
}
