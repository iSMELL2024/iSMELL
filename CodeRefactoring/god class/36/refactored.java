class MenuBarInitializer {
    private int menuShortcut;
    private GenericArgoMenuBar menuBar;

    public MenuBarInitializer(GenericArgoMenuBar menuBar, int menuShortcut) {
        this.menuBar = menuBar;
        this.menuShortcut = menuShortcut;
    }

    public void initMenus() {
        initMenuFile();
        initMenuEdit();
        initMenuView();
        initMenuCreate();
        initMenuArrange();
        initMenuGeneration();
        initMenuCritique();
        initMenuTools();
        initMenuHelp();
    }

    private void initActions() {
        navigateTargetForwardAction = new NavigateTargetForwardAction();
        navigateTargetBackAction = new NavigateTargetBackAction();
        TargetManager.getInstance().addTargetListener(this);
    }
    /**
     * This should be a user specified option. New laws for handicapped people
     * who cannot use the mouse require software developers in US to make all
     * components of User interface accessible through keyboard
     *
     * @param item
     *            is the JMenuItem to do this for.
     * @param key
     *            is the key that we do this for.
     */
    protected static final void setMnemonic(JMenuItem item, String key) {
        String propertykey = "";
        if (item instanceof JMenu) {
            propertykey = MENU + prepareKey(key) + ".mnemonic";
        } else {
            propertykey = MENUITEM + prepareKey(key) + ".mnemonic";
        }

        String localMnemonic = Translator.localize(propertykey);
        char mnemonic = ' ';
        if (localMnemonic != null && localMnemonic.length() == 1) {
            mnemonic = localMnemonic.charAt(0);
        }
        item.setMnemonic(mnemonic);
    }

    /**
     * @param key
     *            the key to localize
     * @return the localized string
     */
    protected static final String menuLocalize(String key) {
        return Translator.localize(MENU + prepareKey(key));
    }

    /**
     * Set the accelerator of a given item.
     *
     * @param item
     *            The item.
     * @param keystroke
     *            The key stroke.
     */
    static final void setAccelerator(JMenuItem item, KeyStroke keystroke) {
        if (keystroke != null) {
            item.setAccelerator(keystroke);
        }
    }

    /**
     * Scans through all loaded modules to see if it has an item to add in this
     * diagram.
     *
     * @param menuitem
     *            The menuitem which this menuitem would attach to.
     * @param key
     *            Non-localized string that tells the module where we are.
     */
    private void appendPluggableMenus(JMenuItem menuitem, String key) {
        Object[] context = {
                menuitem, key,
        };
        List arraylist = Argo.getPlugins(PluggableMenu.class, context);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableMenu module = (PluggableMenu) iterator.next();
            menuitem.add(module.getMenuItem(context));
            menuitem.setEnabled(true);
        }
    }

    /**
     * @see ArgoModuleEventListener#moduleLoaded(ArgoModuleEvent)
     */
    public void moduleLoaded(ArgoModuleEvent event) {
        if (event.getSource() instanceof PluggableMenu) {
            PluggableMenu module = (PluggableMenu) event.getSource();
            Object[] context = new Object[] {
                    tools, "Tools",
            };
            if (module.inContext(context)) {
                tools.add(module.getMenuItem(context));
                tools.setEnabled(true);
            }
            // context = new Object[] { _import, "File:Import" };
            // if (module.inContext(context)) {
            // _import.add(module.getMenuItem(context));
            // }
            context = new Object[] {
                    generate, "Generate",
            };
            if (module.inContext(context)) {
                generate.add(module.getMenuItem(context));
            }
            context = new Object[] {
                    edit, "Edit",
            };
            if (module.inContext(context)) {
                edit.add(module.getMenuItem(context));
            }
            context = new Object[] {
                    view, "View",
            };
            if (module.inContext(context)) {
                view.add(module.getMenuItem(context));
            }
            context = new Object[] {
                    createDiagrams, "Create Diagrams",
            };
            if (module.inContext(context)) {
                createDiagrams.add(module.getMenuItem(context));
            }
            context = new Object[] {
                    arrange, "Arrange",
            };
            if (module.inContext(context)) {
                arrange.add(module.getMenuItem(context));
            }
            context = new Object[] {
                    help, "Help",
            };
            if (module.inContext(context)) {
                if (help.getItemCount() == 1) {
                    help.insertSeparator(0);
                }
                help.insert(module.getMenuItem(context), 0);
            }
        }
    }

    /**
     * @see ArgoModuleEventListener#moduleUnloaded(ArgoModuleEvent)
     */
    public void moduleUnloaded(ArgoModuleEvent event) {
        // TODO: Disable menu
    }

    /**
     * @see ArgoModuleEventListener#moduleEnabled(ArgoModuleEvent)
     */
    public void moduleEnabled(ArgoModuleEvent event) {
        // TODO: Enable menu
    }

    /**
     * @see ArgoModuleEventListener#moduleDisabled(ArgoModuleEvent)
     */
    public void moduleDisabled(ArgoModuleEvent event) {
        // TODO: Disable menu
    }

    private void initMenuFile(int mask) {

        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, mask);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, mask);
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask);
        KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, mask);
        KeyStroke altF4 =
                KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK);

        JMenu file = new JMenu(menuLocalize("File"));
        add(file);
        setMnemonic(file, "File");
        fileToolbar = new ToolBar("File Toolbar");
        JMenuItem newItem = file.add(new ActionNew());
        setMnemonic(newItem, "New");
        setAccelerator(newItem, ctrlN);
        fileToolbar.add((new ActionNew()));
        JMenuItem openProjectItem = file.add(new ActionOpenProject());
        setMnemonic(openProjectItem, "Open");
        setAccelerator(openProjectItem, ctrlO);
        fileToolbar.add(new ActionOpenProject());
        file.addSeparator();

        JMenuItem saveProjectItem =
                file.add(ProjectBrowser.getInstance().getSaveAction());
        setMnemonic(saveProjectItem, "Save");
        setAccelerator(saveProjectItem, ctrlS);
        fileToolbar.add((ProjectBrowser.getInstance().getSaveAction()));
        JMenuItem saveProjectAsItem = file.add(new ActionSaveProjectAs());
        setMnemonic(saveProjectAsItem, "SaveAs");
        JMenuItem revertToSavedItem = file.add(new ActionRevertToSaved());
        setMnemonic(revertToSavedItem, "Revert To Saved");
        file.addSeparator();

        file.add(new ActionImportXMI());
        file.add(new ActionExportXMI());

        JMenuItem importFromSources =
                file.add(ActionImportFromSources.getInstance());
        setMnemonic(importFromSources, "Import");
        file.addSeparator();

        Action a = new ActionProjectSettings();
        fileToolbar.add(a);

        JMenuItem pageSetupItem = file.add(new ActionPageSetup());
        setMnemonic(pageSetupItem, "PageSetup");
        JMenuItem printItem = file.add(new ActionPrint());
        setMnemonic(printItem, "Print");
        setAccelerator(printItem, ctrlP);
        fileToolbar.add((new ActionPrint()));
        JMenuItem saveGraphicsItem = file.add(new ActionSaveGraphics());
        file.add(new ActionSaveAllGraphics());
        setMnemonic(saveGraphicsItem, "SaveGraphics");

        file.addSeparator();

        JMenu notation =
                (JMenu) file.add(new ActionNotation().getMenu());
        setMnemonic(notation, "Notation");

        JMenuItem propertiesItem = file.add(new ActionProjectSettings());
        setMnemonic(propertiesItem, "Properties");

        JMenuItem saveConfiguration = file.add(new ActionSaveConfiguration());
        setMnemonic(saveConfiguration, "Save Configuration");
        file.addSeparator();

        // add last recently used list _before_ exit menu
        lruList = new LastRecentlyUsedMenuList(file);

        // and exit menu entry starting with separator
        file.addSeparator();
        JMenuItem exitItem = file.add(new ActionExit());
        setMnemonic(exitItem, "Exit");
        setAccelerator(exitItem, altF4);
    }

    /**
     * Build the menu "Edit".
     *
     * @param mask
     *            menu shortcut key mask
     */
    private void initMenuEdit(int mask) {

        KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, mask);
        KeyStroke.getKeyStroke(KeyEvent.VK_C, mask);
        KeyStroke.getKeyStroke(KeyEvent.VK_V, mask);
        KeyStroke.getKeyStroke(KeyEvent.VK_X, mask);
        KeyStroke ctrlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y, mask);
        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, mask);
        KeyStroke delKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        KeyStroke ctrlDel = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, mask);

        edit = add(new JMenu(menuLocalize("Edit")));
        setMnemonic(edit, "Edit");

        JMenuItem undoItem =
                edit.add(ProjectBrowser.getInstance().getUndoAction());
        setMnemonic(undoItem, "Undo");
        setAccelerator(undoItem, ctrlZ);
        undoItem.setVisible(UndoEnabler.enabled);

        JMenuItem redoItem =
                edit.add(ProjectBrowser.getInstance().getRedoAction());
        setMnemonic(redoItem, "Redo");
        setAccelerator(redoItem, ctrlY);
        redoItem.setVisible(UndoEnabler.enabled);

        if (UndoEnabler.enabled) {
            edit.addSeparator();
        }

        select = new JMenu(menuLocalize("Select"));
        setMnemonic(select, "Select");
        edit.add(select);

        JMenuItem selectAllItem = select.add(new CmdSelectAll());
        setMnemonic(selectAllItem, "Select All");
        setAccelerator(selectAllItem, ctrlA);
        select.addSeparator();
        JMenuItem backItem = select.add(navigateTargetBackAction);
        setMnemonic(backItem, "Navigate Back");
        // setAccelerator(backItem,altLeft);
        JMenuItem forwardItem = select.add(navigateTargetForwardAction);
        setMnemonic(forwardItem, "Navigate Forward");
        // setAccelerator(forwardItem,altRight);
        select.addSeparator();

        JMenuItem selectInvert = select.add(new CmdSelectInvert());
        setMnemonic(selectInvert, "Invert Selection");

        edit.addSeparator();

        // JMenuItem cutItem = edit.add(ActionCut.getInstance());
        // setMnemonic(cutItem, "Cut");
        // setAccelerator(cutItem, ctrlX);
        //
        // JMenuItem copyItem = edit.add(ActionCopy.getInstance());
        // setMnemonic(copyItem, "Copy");
        // setAccelerator(copyItem, ctrlC);
        //
        // JMenuItem pasteItem = edit.add(ActionPaste.getInstance());
        // setMnemonic(pasteItem, "Paste");
        // setAccelerator(pasteItem, ctrlV);
        //
        // edit.addSeparator();

        Action removeFromDiagram =
                ProjectBrowser.getInstance().getRemoveFromDiagramAction();
        JMenuItem removeItem = edit.add(removeFromDiagram);

        setMnemonic(removeItem, "Remove from Diagram");
        setAccelerator(removeItem, delKey);

        JMenuItem deleteItem =
                edit.add(TargetManager.getInstance().getDeleteAction());
        setMnemonic(deleteItem, "Delete from Model");
        setAccelerator(deleteItem, ctrlDel);

        edit.addSeparator();

        edit.add(new ActionPerspectiveConfig());

        JMenuItem settingsItem = edit.add(new ActionSettings());
        setMnemonic(settingsItem, "Settings");
    }

    /**
     * Build the menu "View".
     *
     * @param mask
     *            menu shortcut key mask
     */
    private void initMenuView(int mask) {

        KeyStroke ctrlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, mask);
        KeyStroke ctrlEquals = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, mask);
        KeyStroke f3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);

        view = (ArgoJMenu) add(new ArgoJMenu(MENU + prepareKey("View")));
        setMnemonic(view, "View");

        JMenuItem gotoDiagram = view.add(new ActionGotoDiagram());
        setMnemonic(gotoDiagram, "Goto-Diagram");

        JMenuItem findItem = view.add(new ActionFind());
        setMnemonic(findItem, "Find");
        setAccelerator(findItem, f3);

        view.addSeparator();

        JMenu zoom = (JMenu) view.add(new JMenu(menuLocalize("Zoom")));
        setMnemonic(zoom, "Zoom");

        JMenuItem zoomOut = zoom.add(new CmdZoom(ZOOM_FACTOR));
        setMnemonic(zoomOut, "Zoom Out");
        zoomOut.setAccelerator(ctrlMinus);

        JMenuItem zoomReset = zoom.add(new CmdZoom(0.0));
        setMnemonic(zoomReset, "Zoom Reset");

        JMenuItem zoomIn = zoom.add(new CmdZoom((1.0) / (ZOOM_FACTOR)));
        setMnemonic(zoomIn, "Zoom In");
        zoomIn.setAccelerator(ctrlEquals);

        view.addSeparator();
        JMenuItem adjustGrid = view.add(new CmdAdjustGrid());
        setMnemonic(adjustGrid, "Adjust Grid");
        JMenuItem adjustGuide = view.add(new CmdAdjustGuide());
        setMnemonic(adjustGuide, "Adjust Guide");
        JMenuItem adjustPageBreaks = view.add(new CmdAdjustPageBreaks());
        setMnemonic(adjustPageBreaks, "Adjust Pagebreaks");

        view.addSeparator();
        JMenuItem showSaved = view.add(new ActionShowXMLDump());
        setMnemonic(showSaved, "Show Saved");

        appendPluggableMenus(view, PluggableMenu.KEY_VIEW);
    }

    /**
     * Build the menu "Create" and the toolbar for diagram creation. These are
     * build together to guarantee that the same items are present in both, and
     * in the same sequence.<p>
     *
     * The sequence of these items was determined by issue 1821.
     */
    private void initMenuCreate() {
        createDiagrams = add(new JMenu(menuLocalize("Create Diagram")));
        setMnemonic(createDiagrams, "Create Diagram");
        createDiagramToolbar = new ToolBar("Create Diagram Toolbar");
        JMenuItem usecaseDiagram =
                createDiagrams.add(new ActionUseCaseDiagram());
        setMnemonic(usecaseDiagram, "Usecase Diagram");
        createDiagramToolbar.add((new ActionUseCaseDiagram()));

        JMenuItem classDiagram = createDiagrams.add(new ActionClassDiagram());
        setMnemonic(classDiagram, "Class Diagram");
        createDiagramToolbar.add((new ActionClassDiagram()));

        JMenuItem sequenzDiagram =
                createDiagrams.add(new ActionSequenceDiagram());
        setMnemonic(sequenzDiagram, "Sequenz Diagram");
        createDiagramToolbar.add((new ActionSequenceDiagram()));

        JMenuItem collaborationDiagram =
                createDiagrams.add(new ActionCollaborationDiagram());
        setMnemonic(collaborationDiagram, "Collaboration Diagram");
        createDiagramToolbar.add((new ActionCollaborationDiagram()));

        JMenuItem stateDiagram = createDiagrams.add(new ActionStateDiagram());
        setMnemonic(stateDiagram, "Statechart Diagram");
        createDiagramToolbar.add((new ActionStateDiagram()));

        JMenuItem activityDiagram =
                createDiagrams.add(new ActionActivityDiagram());
        setMnemonic(activityDiagram, "Activity Diagram");
        createDiagramToolbar.add((new ActionActivityDiagram()));

        JMenuItem deploymentDiagram =
                createDiagrams.add(new ActionDeploymentDiagram());
        setMnemonic(deploymentDiagram, "Deployment Diagram");
        createDiagramToolbar.add((new ActionDeploymentDiagram()));

        appendPluggableMenus(createDiagrams, PluggableMenu.KEY_CREATE_DIAGRAMS);
    }

    /**
     * Build the menu "Arrange".
     */
    private void initMenuArrange() {
        arrange = (ArgoJMenu) add(new ArgoJMenu(MENU + prepareKey("Arrange")));
        setMnemonic(arrange, "Arrange");

        JMenu align = (JMenu) arrange.add(new JMenu(menuLocalize("Align")));
        setMnemonic(align, "Align");
        JMenu distribute =
                (JMenu) arrange.add(new JMenu(
                        menuLocalize("Distribute")));
        setMnemonic(distribute, "Distribute");
        JMenu reorder = (JMenu) arrange.add(new JMenu(menuLocalize("Reorder")));
        setMnemonic(reorder, "Reorder");
        JMenu nudge = (JMenu) arrange.add(new JMenu(menuLocalize("Nudge")));
        setMnemonic(nudge, "Nudge");

        JMenuItem preferredSize =
                arrange.add(new CmdSetPreferredSize(
                        CmdSetPreferredSize.MINIMUM_SIZE));
        setMnemonic(preferredSize, "Preferred Size");

        JMenuItem autoResize = arrange.addCheckItem(new ActionAutoResize());
        setMnemonic(autoResize, "Toggle Auto Resize");

        arrange.add(new ActionLayout());

        appendPluggableMenus(arrange, PluggableMenu.KEY_ARRANGE);

        // This used to be deferred, but it's only 30-40 msec of work.
        InitMenusLater.initMenus(align, distribute, reorder, nudge);
    }


    /**
     * Build the menu "Generation".
     */
    private void initMenuGeneration() {

        KeyStroke f7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);

        generate = add(new JMenu(menuLocalize("Generation")));
        setMnemonic(generate, "Generation");
        JMenuItem genOne = generate.add(new ActionGenerateOne());
        setMnemonic(genOne, "Generate Selected Classes");
        JMenuItem genAllItem = generate.add(new ActionGenerateAll());
        setMnemonic(genAllItem, "Generate all classes");
        setAccelerator(genAllItem, f7);
        generate.addSeparator();
        JMenuItem genProject = generate.add(new ActionGenerateProjectCode());
        setMnemonic(genProject, "Generate code for project");
        JMenuItem generationSettings =
                generate.add(new ActionGenerationSettings());
        setMnemonic(generationSettings, "Settings for project code generation");
        // generate.add(Actions.GenerateWeb);
        appendPluggableMenus(generate, PluggableMenu.KEY_GENERATE);
    }

    /**
     * Build the menu "Critique".
     */
    private void initMenuCritique() {
        critique =
                (ArgoJMenu) add(new ArgoJMenu(MENU + prepareKey("Critique")));
        setMnemonic(critique, "Critique");
        JMenuItem toggleAutoCritique =
                critique.addCheckItem(new ActionAutoCritique());
        setMnemonic(toggleAutoCritique, "Toggle Auto Critique");
        critique.addSeparator();
        JMenuItem designIssues = critique.add(new ActionOpenDecisions());
        setMnemonic(designIssues, "Design Issues");
        JMenuItem designGoals = critique.add(new ActionOpenGoals());
        setMnemonic(designGoals, "Design Goals");
        JMenuItem browseCritics = critique.add(new ActionOpenCritics());
        setMnemonic(browseCritics, "Browse Critics");
    }

    /**
     * Build the menu "Tools".
     */
    private void initMenuTools() {
        tools = new JMenu(menuLocalize("Tools"));
        setMnemonic(tools, "Tools");

        appendPluggableMenus(tools, PluggableMenu.KEY_TOOLS);
        add(tools);
    }

    /**
     * Build the menu "Help".
     */
    private void initMenuHelp() {
        help = new JMenu(menuLocalize("Help"));
        setMnemonic(help, "Help");
        appendPluggableMenus(help, PluggableMenu.KEY_HELP);
        if (help.getItemCount() > 0) {
            help.insertSeparator(0);
        }

        JMenuItem systemInfo = help.add(new ActionSystemInfo());
        setMnemonic(systemInfo, "System Information");
        help.addSeparator();
        JMenuItem aboutArgoUML = help.add(new ActionAboutArgoUML());
        setMnemonic(aboutArgoUML, "About ArgoUML");

        // setHelpMenu(help);
        add(help);
    }
}


// New class to handle target navigation
class TargetNavigator {

    private Action navigateTargetForwardAction;
    private Action navigateTargetBackAction;

    public TargetNavigator() {
        navigateTargetForwardAction = new NavigateTargetForwardAction();
        navigateTargetBackAction = new NavigateTargetBackAction();
        TargetManager.getInstance().addTargetListener(this);
    }

    private void setTarget() {
        navigateTargetForwardAction.setEnabled(
                navigateTargetForwardAction.isEnabled());
        navigateTargetBackAction.setEnabled(
                navigateTargetBackAction.isEnabled());
    }
}

// The GenericArgoMenuBar class now delegates responsibility to the new classes
public class GenericArgoMenuBar extends JMenuBar implements
        TargetListener,
        ArgoModuleEventListener {

    // ... (Other existing code and member variables remain unchanged)
    /**
     * The toolbars.
     */
    private JToolBar fileToolbar;

    private JToolBar editToolbar;

    private JToolBar viewToolbar;

    private JToolBar createDiagramToolbar;

    /**
     * lru project list.
     */
    private LastRecentlyUsedMenuList lruList;
    private JMenu tools;


    // Use the new classes within the GenericArgoMenuBar methods
    private MenuBarInitializer menuBarInitializer;
    private TargetNavigator targetNavigator;

    public GenericArgoMenuBar() {
        this.menuBarInitializer = new MenuBarInitializer();
        this.targetNavigator = new TargetNavigator();

        menuBarInitializer.initMenus();
    }

    // ... (Other methods remain unchanged, but may also delegate to the new classes)
    /**
     * Get the create diagram toolbar.
     *
     * @return Value of property _createDiagramToolbar.
     */
    public JToolBar getCreateDiagramToolbar() {
        return createDiagramToolbar;
    }

    /**
     * Get the edit toolbar.
     *
     * @return the edit toolbar.
     */
    public JToolBar getEditToolbar() {
        if (editToolbar == null) {
            editToolbar = new ToolBar("Edit Toolbar");
            // editToolbar.add(ActionCut.getInstance());
            // editToolbar.add(ActionCopy.getInstance());
            // editToolbar.add(ActionPaste.getInstance());
            editToolbar.addFocusListener(ActionPaste.getInstance());
            editToolbar.add(ProjectBrowser.getInstance()
                    .getRemoveFromDiagramAction());
            editToolbar.add(navigateTargetBackAction);
            editToolbar.add(navigateTargetForwardAction);
        }
        return editToolbar;
    }

    /**
     * Getter for the file toolbar.
     *
     * @return the file toolbar.
     *
     */
    public JToolBar getFileToolbar() {
        return fileToolbar;
    }

    /**
     * Getter for the view toolbar.
     *
     * @return the view toolbar.
     */
    public JToolBar getViewToolbar() {
        if (viewToolbar == null) {
            viewToolbar = new ToolBar("View Toolbar");
            viewToolbar.add(new ActionFind());
            viewToolbar.add(new ZoomSliderButton());
        }
        return viewToolbar;
    }
    /**
     * Adds the entry to the lru list.
     *
     * @param filename
     *            of the project
     */
    public void addFileSaved(String filename) {
        lruList.addEntry(filename);
    }

    /**
     * Getter for the Tools menu.
     *
     * @return The Tools menu.
     */
    public JMenu getTools() {
        return tools;
    }
    /**
     * Prepares one part of the key for menu- or/and menuitem-mnemonics used in
     * menu.properties.
     *
     * The method changes the parameter str to lower cases. Spaces in the
     * parameter str are changed to hyphens.
     *
     * @param str
     * @return the prepared str
     */
    private static String prepareKey(String str) {
        StringBuffer strb = new StringBuffer(str.toLowerCase());
        for (int i = 0; i < (strb.length() - 1); i++) {
            if (strb.charAt(i) == ' ') {
                strb.setCharAt(i, '-');
            }
        }
        return strb.toString();
    }


    public void targetAdded(TargetEvent e) {
        targetNavigator.setTarget();
    }

    public void targetRemoved(TargetEvent e) {
        targetNavigator.setTarget();
    }

    public void targetSet(TargetEvent e) {
        targetNavigator.setTarget();
    }
}