/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.service.ForumStructureService;
import org.jtalks.poulpe.web.controller.SelectedEntity;
import org.jtalks.poulpe.web.controller.WindowManager;
import org.jtalks.poulpe.web.controller.branch.BranchPermissionManagementVm;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;

import javax.annotation.Nonnull;

import static org.jtalks.poulpe.web.controller.section.TreeNodeFactory.buildForumStructure;

/**
 * Is used in order to work with page that allows admin to manage sections and branches (moving them, reordering,
 * removing, editing, etc.). Note, that this class is responsible for back-end of the operations (presenter,
 * controller), so it stores all the changes to the database using {@link ComponentService}.
 *
 * @author stanislav bashkirtsev
 * @author Guram Savinov
 */
public class ForumStructureVm {
    private static final String SELECTED_ITEM_PROP = "selectedItemInTree", TREE_MODEL = "treeModel";
    private final ForumStructureService forumStructureService;
    private final WindowManager windowManager;
    private ForumStructureItem selectedItemInTree = new ForumStructureItem(null);
    private SelectedEntity<PoulpeBranch> selectedBranchForPermissions;
    private ForumStructureTreeModel treeModel;

    public ForumStructureVm(@Nonnull ForumStructureService forumStructureService, @Nonnull WindowManager windowManager,
                            @Nonnull SelectedEntity<PoulpeBranch> selectedBranchForPermissions) {
        this.forumStructureService = forumStructureService;
        this.windowManager = windowManager;
        this.selectedBranchForPermissions = selectedBranchForPermissions;
    }

    /**
     * Creates the whole sections and branches structure. Always hits database. Is executed each time a page is
     * opening.
     */
    @Init
    public void init() {
        treeModel = new ForumStructureTreeModel(buildForumStructure(loadJcommune()));
    }

    @GlobalCommand
    @NotifyChange({TREE_MODEL, SELECTED_ITEM_PROP})
    public void refreshTree() {
    }

    /**
     * Opens a separate page - Branch Permissions where admin can edit what Groups have wha Permissions on the selected
     * branch.
     */
    @Command
    public void openBranchPermissions() {
        selectedBranchForPermissions.setEntity(getSelectedItemInTree().getBranchItem());
        BranchPermissionManagementVm.showPage(windowManager);
    }

    public void removeBranchFromTree(PoulpeBranch branch) {
        treeModel.removeBranch(branch);
    }

    public void removeSectionFromTree(PoulpeSection section) {
        treeModel.removeSection(section);
    }

    public void updateBranchInTree(PoulpeBranch branch) {
        treeModel.moveBranchIfSectionChanged(branch);
        selectedItemInTree = new ForumStructureItem(branch);
    }

    public void updateSectionInTree(PoulpeSection section) {
        treeModel.addIfAbsent(section);
        selectedItemInTree = new ForumStructureItem(section);
    }

    public ForumStructureItem getSelectedItemInTree() {
        return selectedItemInTree;
    }

    /**
     * Is used by ZK binder to inject the section that is currently selected.
     *
     * @param selectedNode the section that is currently selected
     */
    @NotifyChange(SELECTED_ITEM_PROP)
    public void setSelectedNode(TreeNode<ForumStructureItem> selectedNode) {
        this.selectedItemInTree = selectedNode.getData();
    }

    /**
     * Loads instance of JCommune from database.
     *
     * @return instance of JCommune from database
     */
    private Jcommune loadJcommune() {
        Jcommune jcommune = forumStructureService.getJcommune();
        if (jcommune == null) {
            throw new IllegalStateException("Please, create a Forum Component first.");
        }
        return jcommune;
    }

    /**
     * Handler of tree nodes drag and drop event.
     *
     * @param event contains all needed info about drag and drop event
     */
    @Command
    @NotifyChange({TREE_MODEL, SELECTED_ITEM_PROP})
    public void dropEventHandler(@BindingParam("event") DropEvent event) {
        TreeNode<ForumStructureItem> draggedNode = ((Treeitem) event.getDragged()).getValue();
        TreeNode<ForumStructureItem> targetNode = ((Treeitem) event.getTarget()).getValue();
        ForumStructureItem draggedItem = draggedNode.getData();
        if (draggedItem.isBranch()) {
            onDropBranch(draggedNode, targetNode);
        } else if (draggedItem.isSection()) {
            onDropSection(draggedNode, targetNode);
        }
    }

    /**
     * Handler when one tree node dragged and dropped to another.
     * 
     * @param dragged the dragged tree node
     * @param target the target tree node
     */
    public void onDropItem(TreeNode<ForumStructureItem> draggedNode,
            TreeNode<ForumStructureItem> targetNode) {
        ForumStructureItem draggedItem = draggedNode.getData();
        if (draggedItem.isBranch()) {
            onDropBranch(draggedNode, targetNode);
        } else if (draggedItem.isSection()) {
            onDropSection(draggedNode, targetNode);
        }
    }

    /**
     * Handler of event when branch node dragged and dropped to another node.
     * 
     * @param draggedNode the node represents dragged branch item
     * @param targetNode the node represents target item (it can be branch or section item)
     */
    private void onDropBranch(TreeNode<ForumStructureItem> draggedNode,
            TreeNode<ForumStructureItem> targetNode) {
        ForumStructureItem targetItem = targetNode.getData();
        PoulpeBranch draggedBranch = draggedNode.getData().getBranchItem();
        if (treeModel.noEffectAfterDropBranch(draggedBranch, targetItem)) {
            return;
        }
        if (targetItem.isBranch()) {
            forumStructureService.moveBranch(draggedBranch, targetItem.getBranchItem());
            treeModel.dropNodeBefore(draggedNode, targetNode);
            setSelectedNode(draggedNode);
        }
        else {
            forumStructureService.moveBranch(draggedBranch, targetItem.getSectionItem());
            treeModel.dropNodeIn(draggedNode, targetNode);
            setSelectedNode(draggedNode);
        }
    }

    /**
     * Handler of event when section node dragged and dropped to another section node.
     * 
     * @param draggedNode the node represents dragged section item
     * @param targetNode the node represents target section item
     */
    private void onDropSection(TreeNode<ForumStructureItem> draggedNode,
            TreeNode<ForumStructureItem> targetNode) {
        ForumStructureItem draggedItem = draggedNode.getData();
        ForumStructureItem targetItem = targetNode.getData();
        if (treeModel.noEffectAfterDropSection(draggedItem, targetItem)) {
            return;
        }
        
        PoulpeSection draggedSection = draggedItem.getSectionItem();
        PoulpeSection targetSection = targetItem.getSectionItem();
        Jcommune jcommune = treeModel.getRootAsJcommune();
        jcommune.moveSection(draggedSection, targetSection);
        forumStructureService.saveJcommune(jcommune);
        treeModel.dropNodeBefore(draggedNode, targetNode);
        setSelectedNode(draggedNode);
    }

    public ForumStructureTreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(ForumStructureTreeModel treeModel) {
        this.treeModel = treeModel;
    }
}
