package org.nuxeo.dam.platform.action;

import static org.jboss.seam.ScopeType.CONVERSATION;
import static org.jboss.seam.ScopeType.EVENT;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.dam.webapp.contentbrowser.DocumentActions;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ActionContext;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.ui.web.util.SeamContextHelper;
import org.nuxeo.ecm.webapp.action.WebActionsBean;
import org.nuxeo.runtime.api.Framework;

/**
 *
 * @author <a href="mailto:cbaican@nuxeo.com">Catalin Baican</a>
 *
 */
@Name("webActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION)
public class DamWebActionsBean extends WebActionsBean {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(DamWebActionsBean.class);

    @In(create = true)
    private transient NuxeoPrincipal currentNuxeoPrincipal;

    @In(create = true, required = false)
    private transient CoreSession documentManager;

    @In(create = true, required = false)
    private transient DocumentActions documentActions;

    private ActionManager actionService;

    private Boolean showList = Boolean.TRUE;

    private Boolean showThumbnail = Boolean.FALSE;

    @Override
    @Factory(value = "tabsActionsList", scope = EVENT)
    public List<Action> getTabsList() {
        if (tabsActionsList == null) {
            actionService = getActionsService();
            tabsActionsList = actionService.getActions(
                    "VIEW_ASSET_ACTION_LIST", createActionContext());
            currentTabAction = getDefaultTab();
        }
        return tabsActionsList;
    }

    public ActionManager getActionsService() {
        if (actionService != null) {
            return actionService;
        }
        try {
            actionService = Framework.getService(ActionManager.class);
        } catch (Exception e) {
            log.error("Failed to lookup ActionService", e);
        }

        return actionService;
    }

    @Override
    protected ActionContext createActionContext() {
        ActionContext ctx = new ActionContext();
        // ctx.setCurrentDocument(navigationContext.getCurrentDocument());
        ctx.setDocumentManager(documentManager);
        ctx.put("SeamContext", new SeamContextHelper());
        ctx.setCurrentPrincipal(currentNuxeoPrincipal);
        return ctx;
    }

    public void showListLink() {
        documentActions.setCurrentSelection(null);
        if (showList) {
            return;
        }
        this.showList = !this.showList;
        this.showThumbnail = !this.showThumbnail;
    }

    public void showThumbnailLink() {
        documentActions.setCurrentSelection(null);
        if (showThumbnail) {
            return;
        }
        this.showThumbnail = !this.showThumbnail;
        this.showList = !this.showList;
    }

    public Boolean getShowList() {
        return showList;
    }

    public void setShowList(Boolean showList) {
        this.showList = showList;
    }

    public Boolean getShowThumbnail() {
        return showThumbnail;
    }

    public void setShowThumbnail(Boolean showThumbnail) {
        this.showThumbnail = showThumbnail;
    }

}
