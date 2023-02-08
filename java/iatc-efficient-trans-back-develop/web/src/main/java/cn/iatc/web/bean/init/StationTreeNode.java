package cn.iatc.web.bean.init;

import cn.iatc.web.common.tree.data.BaseNode;
import lombok.Data;

@Data
public class StationTreeNode<T, E> extends BaseNode<T, E> {
    private String name;
    private String regionCode;
    private Integer sort;
}
