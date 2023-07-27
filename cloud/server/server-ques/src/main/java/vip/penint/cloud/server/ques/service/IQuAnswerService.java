package vip.penint.cloud.server.ques.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.server.ques.entity.QuAnswer;

import java.util.List;

/**
 * <p>
 * 候选答案 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-03
 */
public interface IQuAnswerService extends IService<QuAnswer> {

    List<QuAnswer> getByQuId(String id);

    void saveAll(String id, List<QuAnswer> answerList);

    void removeByQuId(String id);

    /**
     * 根据题目ID查询答案并随机
     *
     * @param quId
     * @return
     */
    List<QuAnswer> listAnswerByRandom(String quId);
}
