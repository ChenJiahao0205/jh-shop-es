package pers.jhshop.es.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.jhshop.common.utils.JhShopDateUtil;
import pers.jhshop.es.model.es.EsProductsEntity;
import pers.jhshop.es.model.req.EsProductsCreateOrUpdateReq;
import pers.jhshop.es.model.req.EsProductsQueryReq;
import pers.jhshop.es.model.vo.EsProductsQueryVo;
import pers.jhshop.es.service.ProductsService;
import pers.jhshop.es.service.es.EsProductsServiceRestTemplateService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 * @author ChenJiahao(五条)
 * @date 2024/12/21 16:39:52
 */
@Service
@Slf4j
@AllArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private EsProductsServiceRestTemplateService esProductsServiceRestTemplateService;

    @Override
    public void createOrUpdateBiz(EsProductsCreateOrUpdateReq createReq) {
        esProductsServiceRestTemplateService.insertOrUpdate(convertReqToEntity(createReq));
    }

    @Override
    public EsProductsQueryVo getByIdBiz(Long id) {
        EsProductsEntity byId = esProductsServiceRestTemplateService.getById(id);
        return convertEsProductsEntityToVo(byId);
    }

    @Override
    public Page<EsProductsEntity> pageBiz(EsProductsQueryReq queryReq) {
        return esProductsServiceRestTemplateService.page(queryReq);
    }

    @Override
    public void batchInsertOrUpdate(List<EsProductsCreateOrUpdateReq> dataList) {
        if (CollectionUtils.isEmpty(dataList)){
            return;
        }

        // 转换请求对象
        List<EsProductsEntity> esProductsEntities = dataList.stream()
                .map(this::convertReqToEntity)
                .collect(Collectors.toList());

        esProductsServiceRestTemplateService.batchInsertOrUpdate(esProductsEntities);
    }

    @Override
    public List<EsProductsEntity> list(EsProductsQueryReq queryReq) {
        return esProductsServiceRestTemplateService.list(queryReq);
    }

    private EsProductsEntity convertReqToEntity(EsProductsCreateOrUpdateReq data) {
        EsProductsEntity esProductsEntity = new EsProductsEntity();
        BeanUtil.copyProperties(data, esProductsEntity, "createdAt", "updatedAt", "createTime", "updateTime");
        // 特殊字段处理
        esProductsEntity.setCreatedAt(JhShopDateUtil.getStrTime(data.getCreatedAt()));
        esProductsEntity.setUpdatedAt(JhShopDateUtil.getStrTime(data.getUpdatedAt()));
        esProductsEntity.setCreateTime(JhShopDateUtil.getStrTime(data.getCreateTime()));
        esProductsEntity.setUpdateTime(JhShopDateUtil.getStrTime(data.getUpdateTime()));
        return esProductsEntity;
    }

    /**
     * 转换entity为vo
     */
    private EsProductsQueryVo convertEsProductsEntityToVo(EsProductsEntity byId) {
        EsProductsQueryVo vo = new EsProductsQueryVo();
        BeanUtil.copyProperties(byId, vo, "createdAt", "updatedAt", "createTime", "updateTime");
        // 特殊字段处理
        vo.setCreatedAt(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getCreatedAt())));
        vo.setUpdatedAt(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getUpdatedAt())));
        vo.setCreateTime(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getCreateTime())));
        vo.setUpdateTime(DateUtil.date(JhShopDateUtil.getDateByStrTime(byId.getUpdateTime())));
        return vo;
    }
}
