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

import java.util.List;

import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.web.controller.ZkHelper;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

/**
 * This class implementation for Delete section dialog
 * 
 * @author Bekrenev Dmitry
 * @author Alexey Grigorev
 */
public class ZkDeleteSectionDialogView extends Window implements DeleteSectionDialogView, AfterCompose {

    private static final long serialVersionUID = -4999382692611273729L;

    private ZkHelper zkHelper = new ZkHelper(this);

    private Radiogroup deleteMode;
    private Radio removeAndMoveMode;
    private Combobox selectedSection;

    private DeleteSectionDialogPresenter presenter;

    private PoulpeSection sectionToDelete;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompose() {
        zkHelper.wireByConvention();

        presenter.setView(this);
        selectedSection.setItemRenderer(new SectionComboboxItemRenderer());
    }

    /**
     * Renderer for combobox items with sections
     */
    static class SectionComboboxItemRenderer implements ComboitemRenderer<PoulpeSection> {
        @Override
        public void render(Comboitem item, PoulpeSection section, int index) throws Exception {
            item.setLabel(section.getName());
            item.setDescription(section.getDescription());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeSection getSectionToDelete() {
        return sectionToDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PoulpeSection getRecipientSection() {
        return (PoulpeSection) selectedSection.getModel().getElementAt(selectedSection.getSelectedIndex());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SectionDeleteMode getDeleteMode() {
        String mode = getDeleteModeValue();
        return SectionDeleteMode.fromString(mode);
    }

    /**
     * @return delete mode 
     * @see SectionDeleteMode
     */
    private String getDeleteModeValue() {
        return deleteMode.getItemAtIndex(deleteMode.getSelectedIndex()).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDialog(PoulpeSection section) {
        sectionToDelete = section;
        showDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDialog() {
        presenter.refreshSectionsCombobox();

        useFirstSectionAsDefaultRecipient();
        deleteMode.setSelectedIndex(0);

        show();
    }

    /**
     * Choice default section in combobox
     */
    private void useFirstSectionAsDefaultRecipient() {
        @SuppressWarnings("unchecked")
        ListModelList<PoulpeSection> model = (ListModelList<PoulpeSection>) selectedSection.getModel();
        model.clearSelection();

        if (!model.isEmpty()) {
            model.addToSelection(model.get(0));
        } else {
            model.addToSelection(null);
        }

        selectedSection.setModel(model);
    }

    /**
     * Shows the dialog
     */
    private void show() {
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeDialog() {
        setVisible(false);
    }

    /**
     * Deletes selected section
     */
    public void deleteSection() {
        presenter.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSectionsCombobox(List<PoulpeSection> selectableSections) {
        ListModelList<PoulpeSection> modelList = new ListModelList<PoulpeSection>(selectableSections);
        modelList.remove(sectionToDelete);

        selectedSection.setModel(modelList);
        selectedSection.setDisabled(false);
        selectedSection.setRawValue(null);

        removeAndMoveMode.setDisabled(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initEmptyAndDisabledCombobox() {
        selectedSection.setModel(null);
        selectedSection.setDisabled(true);
        removeAndMoveMode.setDisabled(true);
    }

    /**
     * This event cause show dialog
     * 
     * @param event information about event contain PoulpeSection which will be
     * deleted
     * @deprecated use {@link #showDialog(PoulpeSection)} instead
     */
    @Deprecated
    public void onOpenDeleteSectionDialog(Event event) {
        showDialog((PoulpeSection) event.getData());
    }

    /**
     * This event cause delete
     */
    public void onClick$confirmButton() {
        deleteSection();
    }

    /**
     * This event cause close dialog
     */
    public void onClick$rejectButton() {
        closeDialog();
    }

    /**
     * @param presenter DeleteSectionDialogPresenter instance
     */
    public void setPresenter(DeleteSectionDialogPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Package-private for DI in tests
     * @param zkHelper instance of helper
     */
    void setZkHelper(ZkHelper zkHelper) {
        this.zkHelper = zkHelper;
    }

    /**
     * Package-private for DI in tests
     * @param deleteMode radio group with modes
     * @param removeAndMoveMode radio to select delete mode (see {@link SectionDeleteMode}
     * @param selectedSection combobox with sections
     */
    void setUiElements(Radiogroup deleteMode, Radio removeAndMoveMode, Combobox selectedSection) {
        this.deleteMode = deleteMode;
        this.removeAndMoveMode = removeAndMoveMode;
        this.selectedSection = selectedSection;
    }

}
