package pl.poznan.model;

import io.jenetics.ext.util.TreeFormatter;
import io.jenetics.ext.util.TreeNode;

public class GPTreeResult {

    private Double evaluation;
    private TreeNode tree;

    public Double getEvaluation() {
        return evaluation;
    }

    public TreeNode getTree() {
        return tree;
    }

    public GPTreeResult(Double evaluation, TreeNode tree) {
        this.evaluation = evaluation;
        this.tree = tree;
    }

    @Override
    public String toString() {
        return TreeFormatter.TREE.format(this.tree);
    }
}
