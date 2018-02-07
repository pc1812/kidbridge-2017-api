package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Bill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用明细
 */

@Service
public class BillService {

    private final Logger logger = LogManager.getLogger(BillService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.bill.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public Bill get(Bill param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<Bill> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Bill());
    }

    @Deprecated
    public List<Bill> list(Bill param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    /**
     * 获取费用明细列表
     * @param param
     * @param page
     * @return
     */
    public List<Bill> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 新增收支明细
     * @param param
     * @return
     */
    public Integer add(Bill param){
        return this.sqlSessionTemplate.insert(this.namespace + "add",param);
    }

    @Deprecated
    public BigDecimal sum(Bill param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "sum",param);
    }

}
