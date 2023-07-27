package vip.penint.cloud.server.ques.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.server.ques.entity.QuAnswer;
import vip.penint.cloud.server.ques.mapper.IQuAnswerMapper;
import vip.penint.cloud.server.ques.service.IQuAnswerService;
import vip.penint.cloud.server.ques.utils.BeanMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 候选答案 服务实现类
 * </p>
 *
 * @author Penint
 * @since 2022-11-03
 */
@Service
public class QuAnswerServiceImpl extends ServiceImpl<IQuAnswerMapper, QuAnswer> implements IQuAnswerService {


    @Override
    public List<QuAnswer> getByQuId(String id) {
        LambdaQueryWrapper<QuAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuAnswer::getQuId, id);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void saveAll(String quId, List<QuAnswer> answerList) {
        //最终要保存的列表
        List<QuAnswer> saveList = new ArrayList<>();

        //已存在的标签列表
        List<String> ids = this.findExistsList(quId);

        if (!CollectionUtils.isEmpty(answerList)) {
            for (QuAnswer item : answerList) {

                //标签ID
                String id = item.getId();
                QuAnswer answer = new QuAnswer();
                BeanMapper.copy(item, answer);
                answer.setQuId(quId);

                //补全ID避免新增
                if (ids.contains(id)) {
                    ids.remove(id);
                }

                saveList.add(answer);
            }

            //保存标签列表
            if (!CollectionUtils.isEmpty(saveList)) {
                this.saveOrUpdateBatch(saveList);
            }

            //删除已移除
            if (!ids.isEmpty()) {
                this.removeByIds(ids);
            }
        } else {

            QueryWrapper<QuAnswer> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(QuAnswer::getQuId, quId);
            this.remove(wrapper);
        }
    }

    /**
     * 查找已存在的列表
     *
     * @param quId
     * @return
     */
    public List<String> findExistsList(String quId) {
        //返回结果
        List<String> ids = new ArrayList<>();

        QueryWrapper<QuAnswer> wrapper = new QueryWrapper();
        wrapper.lambda().eq(QuAnswer::getQuId, quId);
        List<QuAnswer> list = this.list(wrapper);

        if (!CollectionUtils.isEmpty(list)) {
            for (QuAnswer item : list) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

    @Override
    public void removeByQuId(String id) {
        LambdaQueryWrapper<QuAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuAnswer::getQuId, id);
        this.remove(wrapper);
    }

    @Override
    public List<QuAnswer> listAnswerByRandom(String quId) {
        QueryWrapper<QuAnswer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QuAnswer::getQuId, quId);
        wrapper.last(" ORDER BY RAND() ");

        return this.list(wrapper);
    }
}
