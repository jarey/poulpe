package org.jtalks.poulpe.web.controller.branch;

import com.google.common.collect.Lists;
import org.jtalks.poulpe.model.entity.Branch;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.security.BranchPermission;
import org.jtalks.poulpe.service.security.JtalksPermission;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementBlock;
import org.jtalks.poulpe.web.controller.zkmacro.BranchPermissionManagementRow;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import java.util.LinkedList;
import java.util.List;

/**
 * A View Model for page that allows user to specify what actions can be done with the specific branch and what
 * user groups can do them.
 *
 * @author stanislav bashkirtsev
 */
public class BranchPermissionManagementVm {
    private List<BranchPermissionManagementBlock> blocks = new LinkedList<BranchPermissionManagementBlock>();
    private Branch branch;

    public BranchPermissionManagementVm() {
        List<BranchPermissionManagementRow> rows = new LinkedList<BranchPermissionManagementRow>();
        rows.add(new BranchPermissionManagementRow("Allowed", Lists.newArrayList(new Group("Moderators", ""))));
        rows.add(new BranchPermissionManagementRow("Restricted", Lists.newArrayList(new Group
                ("Moderators", ""), new Group("Registered Users", ""), new Group("Activated Users", ""))));
        blocks.add(new BranchPermissionManagementBlock(BranchPermission.CREATE_TOPICS, rows.get(0), rows.get(1)));
        blocks.add(new BranchPermissionManagementBlock(BranchPermission.CREATE_TOPICS, rows.get(0), rows.get(1)));
    }
    @Command
    public void showGroupsDialog(){
//        Executions.createComponents()
        Executions.createComponents("/sections/ManageGroupsDialog.zul", null,null);
    }

    public ListModel getBlocksListModel() {
        return new BindingListModelList(blocks, true);
    }

    public List<BranchPermissionManagementBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BranchPermissionManagementBlock> blocks) {
        this.blocks = blocks;
    }
}
