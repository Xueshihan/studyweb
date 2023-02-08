package cn.iatc.web.common.tree;

import cn.iatc.web.common.tree.data.BaseNode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//图层结构 树形结构  https://www.cnblogs.com/JohanChan/p/12618486.html
@Slf4j
public class TreeBuilderTest<T, K> {

    List<BaseNode<T, K>> nodes = new ArrayList<>();

    Map<T, BaseNode<T, K>> nodeMap = new HashMap<>();

    public TreeBuilderTest() {}

    public TreeBuilderTest(List<BaseNode<T, K>> nodes) {
        this.nodes = nodes;
    }

    public String buildTreeJson(List<BaseNode<T, K>> nodes) {
        TreeBuilderTest<T, K> treeBuilder = new TreeBuilderTest<>(nodes);
        return treeBuilder.buildJSONTree();
    }

    public List<BaseNode<T, K>> buildTreeList(List<BaseNode<T, K>> nodes) {
        TreeBuilderTest<T, K> treeBuilder = new TreeBuilderTest<>(nodes);
        return treeBuilder.buildTree();
    }

    // 构建JSON树形结构
    public String buildJSONTree() {
        List<BaseNode<T, K>> nodeTree = buildTree();
        return JSON.toJSONString(nodeTree);
    }

    // 构建树形结构
    public List<BaseNode<T, K>> buildTree() {
        List<BaseNode<T, K>> treeNodes = new ArrayList<>();
        List<BaseNode<T, K>> rootNodes = getRootNodes();
        log.info("====rootNodes:{}", rootNodes);
        rootNodes.forEach(rootNode -> {
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        });
//        for (BaseNode<T, K> rootNode : rootNodes) {
//            buildChildNodes(rootNode);
//            treeNodes.add(rootNode);
//        }
        return treeNodes;
    }

    // 递归子节点, 找出该节点下的所有子节点
    private void buildChildNodes(BaseNode<T, K> node) {
        handleSubLevel(node);
        List<BaseNode<T, K>> children = getChildNodes(node);
        if (!children.isEmpty()) {
            node.setChildren(children);
            node.setLeafStatus(0);//有下级

            children.forEach(this::buildChildNodes);
//            for (BaseNode<T, K> child : children) {
//                buildChildNodes(child);
//            }
        }else{
            List<BaseNode<T, K>> temp = new ArrayList<>();
            node.setChildren(temp);
            node.setLeafStatus(1);//无下级
        }
    }

    private void handleSubLevel(BaseNode<T, K> node) {
        T curId = node.getId();
        T parentId = node.getParentId();
        if (parentId != null) {
            BaseNode<T, K> parentNode = nodeMap.get(parentId);
            if(parentNode != null) {
                Integer parentLevel = parentNode.getSubLevel();
                Integer subLevel = parentLevel + 1;
                node.setSubLevel(subLevel);
                BaseNode<T, K> subNode = nodeMap.get(curId);
                subNode.setSubLevel(subLevel);
            }
        }
    }

    // 获取父节点下所有的子节点
    private List<BaseNode<T, K>> getChildNodes(BaseNode<T, K> node) {

        return nodes.stream().filter(oldBaseNode -> node.getId().equals(oldBaseNode.getParentId())).collect(Collectors.toList());

//        List<BaseNode<T, K>> childNodes = new ArrayList<>();
//        for (BaseNode<T, K> n : nodes) {
//            if (node.getId().equals(n.getParentId())) {
//                childNodes.add(n);
//            }
//        }
//        return childNodes;
    }

    // 判断是否为根节点
    private boolean rootNode(BaseNode<T, K> node) {
        boolean isRootNode = true;
        for (BaseNode<T, K> n : nodes) {
            T parentId = node.getParentId();
            if (parentId != null && parentId.equals(n.getId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    // 获取集合中所有的根节点
    private List<BaseNode<T, K>> getRootNodes() {
        List<BaseNode<T, K>> rootNodes = new ArrayList<>();
        nodes.forEach(tkBaseNode -> {
            if(rootNode(tkBaseNode)) {
                tkBaseNode.setSubLevel(1);
                rootNodes.add(tkBaseNode);
            }
            nodeMap.put(tkBaseNode.getId(), tkBaseNode);
        });

//        for (BaseNode<T, K> n : nodes) {
//            if (rootNode(n)) {
//                n.setSubLevel(1);
//                rootNodes.add(n);
//            }
//            nodeMap.put(n.getId(), n);
//        }
        return rootNodes;
    }

    // 根节点中，本身id和父id是一个值，上面的算法根节点中，本身id和父id不是一个
    public static <T, K> List<BaseNode<T, K>> build(List<BaseNode<T, K>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<BaseNode<T, K>> topNodes = new ArrayList<>();

        for (BaseNode<T, K> child : nodes) {

            T pid = child.getParentId();
            if (pid.equals(child.getId())) {
                List<BaseNode<T, K>> children = new ArrayList<>();
                child.setChildren(children);
                topNodes.add(child);
                continue;
            }
            for (BaseNode<T, K> parent : nodes) {
                T id = parent.getId();
                List<BaseNode<T, K>> children = parent.getChildren();
                if(children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                if (id != null && id.equals(pid)) {
                    children.add(child);
                }
            }
        }
        return topNodes;
    }
}
