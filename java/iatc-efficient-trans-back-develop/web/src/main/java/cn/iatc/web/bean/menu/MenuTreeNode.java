package cn.iatc.web.bean.menu;

import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.constants.CommonConstants;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MenuTreeNode<T, E> extends BaseNode<T, E> {

    private T id;

    private String name;
    private Meta meta;

    private Integer type;

    private String path;

    private Integer isSelect = CommonConstants.Status.DISABLED.getValue();
}

