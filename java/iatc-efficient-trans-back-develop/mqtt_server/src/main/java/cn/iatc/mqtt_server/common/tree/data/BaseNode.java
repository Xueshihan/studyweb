package cn.iatc.mqtt_server.common.tree.data;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * base节点, 节点和父节点类型一致
 * @param <T> 节点id
 * @param <T> 节点父id
 * @param <K> 级别
 */
public class BaseNode<T, K> {
    // 节点id
    private T id;

    // 上级id
    @Schema(name = "parent_id", description = "父id")
    private T parentId;

    // 级别
    @Schema(description = "级别")
    private K level;

    @Schema(name = "sub_level", description = "子级别，暂时无用")
    private Integer subLevel = -1;

    //孩子节点
    @Schema(defaultValue = "[]")
    private List<BaseNode<T, K>> children = new ArrayList<>();

    @Schema(name = "leaf_status")
    private Integer leafStatus;//是否还有children下转，1代表有children，0代表没有children

    public BaseNode() {}

    public BaseNode(T id, T parentId, K level){
        this.id = id;
        this.parentId = parentId;
        this.level = level;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public T getParentId() {
        return parentId;
    }

    public void setParentId(T parentId) {
        this.parentId = parentId;
    }

    public K getLevel() {
        return level;
    }

    public void setLevel(K level) {
        this.level = level;
    }

    public Integer getSubLevel() {
        return subLevel;
    }

    public void setSubLevel(Integer subLevel) {
        this.subLevel = subLevel;
    }

    public List<BaseNode<T, K>> getChildren() {
        return children;
    }

    public void setChildren(List<BaseNode<T, K>> children) {
        this.children = children;
    }

    public Integer getLeafStatus() {
        return leafStatus;
    }

    public void setLeafStatus(Integer leafStatus) {
        this.leafStatus = leafStatus;
    }
}
