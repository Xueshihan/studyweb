package cn.iatc.web.bean.init;

import cn.iatc.web.common.tree.data.BaseNode;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class RegionTreeNode<T, E> extends BaseNode<T, E> {
    private String name;

    private String pinyin;

    private Integer sort;

    private String nationalCode;

}
