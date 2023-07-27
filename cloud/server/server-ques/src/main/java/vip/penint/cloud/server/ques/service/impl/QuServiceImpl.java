package vip.penint.cloud.server.ques.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.ques.entity.Qu;
import vip.penint.cloud.server.ques.entity.QuAnswer;
import vip.penint.cloud.server.ques.mapper.IQuMapper;
import vip.penint.cloud.server.ques.service.IQuAnswerService;
import vip.penint.cloud.server.ques.service.IQuService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目表 服务实现类
 * </p>
 *
 * @author Penint
 * @since 2022-11-03
 */
@Service
public class QuServiceImpl extends ServiceImpl<IQuMapper, Qu> implements IQuService {


    @Autowired
    private IQuAnswerService answerService;


    /**
     * 分页查询题目列表
     *
     * @param qu 题目
     * @return 题目
     */
    @Override
    public IPage<Qu> selectTQuList(Qu qu, QueryRequest queryRequest) {
        IPage<Qu> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<Qu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(StringUtils.isNotEmpty(qu.getContent()), Qu::getContent, qu.getContent())
                .eq(StringUtils.isNotNull(qu.getQuType()), Qu::getQuType, qu.getQuType())
                .eq(StringUtils.isNotNull(qu.getLevel()), Qu::getLevel, qu.getLevel())
                .orderByDesc(Qu::getCreateTime);

        IPage<Qu> pages = baseMapper.selectPage(page, lambdaQueryWrapper);
        pages.getRecords().forEach(e -> {
            e.setPublishName("系统");
        });
        return pages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQu(Qu qu) throws Exception {
        // 校验数据
        this.checkData(qu, "");

        this.saveOrUpdate(qu);
//        // 归属题库Id list
//        List<String> repoIds = tQu.getRepoIds();
//        List<TQuRepo> repoList = new ArrayList<>();
//        for (String repoId : repoIds) {
//            TQuRepo repo = new TQuRepo();
//            repo.setQuId(tQu.getId());
//            repo.setRepoId(repoId);
//            repo.setQuType(tQu.getQuType());
//            repoList.add(repo);
//        }
//        quRepoService.saveBatch(repoList);
        // 保存全部问题
        answerService.saveAll(qu.getId(), qu.getAnswerList());

        // 保存到题库
//        quRepoService.saveAll(qu.getId(), qu.getQuType(), qu.getRepoIds());
//        // 候选答案列表
//        List<TQuAnswer> answerList = tQu.getAnswerList();
//        // Stream 流 往数组对象中set quId
//        answerList = answerList.stream().map(s -> s.setQuId(tQu.getId())).collect(Collectors.toList());
//        quAnswerService.saveBatch(answerList);
    }

    @Override
    public Qu getInfoById(String id) {

        Qu qu = baseMapper.selectById(id);
//        qu.setRepoIds(quRepoService.getByQuId(id));
        qu.setAnswerList(answerService.getByQuId(id));
        return qu;
    }

    /**
     * 校验题目信息
     *
     * @param qu
     * @param no
     * @throws Exception
     */
    public void checkData(Qu qu, String no) throws Exception {


        if (org.springframework.util.StringUtils.isEmpty(qu.getContent())) {
            throw new Exception(no + "题目内容不能为空！");
        }


//        if (CollectionUtils.isEmpty(qu.getRepoIds())) {
//            throw new Exception(no + "至少要选择一个题库！");
//        }

        List<QuAnswer> answers = qu.getAnswerList();


        if (CollectionUtils.isEmpty(answers)) {
            throw new Exception(no + "客观题至少要包含一个备选答案！");
        }


//        int trueCount = 0;
        for (QuAnswer a : answers) {

//            if (a.getIsRight() == null) {
//                throw new ExamException(no + "必须定义选项是否正确项！");
//            }

            if (org.springframework.util.StringUtils.isEmpty(a.getContent())) {
                throw new Exception(no + "选项内容不为空！");
            }

//            if (a.getIsRight()) {
//                trueCount += 1;
//            }
        }

//        if (trueCount == 0) {
//            throw new ExamException(no + "至少要包含一个正确项！");
//        }


//        //单选题
//        if (qu.getQuType().equals(QuType.RADIO) && trueCount > 1) {
//            throw new ExamException(no + "单选题不能包含多个正确项！");
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByIds(List<String> ids) {

        for (String id : ids) {
            // 通过quId删除试题题库
//            quRepoService.removeByQuId(id);
            // 通过quId删除候选答案
            answerService.removeByQuId(id);
            // 删除题目
            this.removeById(id);
        }
        // 更新全部题库的统计数量
//        repoService.refreshAllStat();
    }

    @Override
    public List<Map<String, Object>> quCount() {
        return baseMapper.quCount();
    }

    @Override
    public Integer selectListByDay(String day) {
        return baseMapper.selectListByDay(day);
    }
}
