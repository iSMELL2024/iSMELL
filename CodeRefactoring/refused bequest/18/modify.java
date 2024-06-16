// Separate class to handle GoStimulusToAction specific functionalities
class StimulusActionHandler {

    public Collection getChildren(Object parent) {
        if (!Model.getFacade().isAStimulus(parent)) {
            return Collections.emptyList();
        }
        Object ms = parent;
        Object action = Model.getFacade().getDispatchAction(ms);
        Vector vector = new Vector();
        vector.addElement(action);
        return vector;
    }

    public Set getDependencies(Object parent) {
        if (Model.getFacade().isAStimulus(parent)) {
            Set set = new HashSet();
            set.add(parent);
            return set;
        }
        return Collections.emptySet();
    }

    public String getRuleName() {
        return Translator.localize("misc.stimulus.action");
    }
}

// Refactored GoStimulusToAction class without inheritance
public class GoStimulusToAction {
    private StimulusActionHandler handler;

    public GoStimulusToAction() {
        this.handler = new StimulusActionHandler();
    }

    /**
     * Gets the children of the given parent.
     *
     * @param parent the parent object
     * @return a collection of child objects
     */
    public Collection getChildren(Object parent) {
        return handler.getChildren(parent);
    }

    /**
     * Gets the dependencies of the given parent.
     *
     * @param parent the parent object
     * @return a set of dependencies
     */
    public Set getDependencies(Object parent) {
        return handler.getDependencies(parent);
    }

    /**
     * Gets the rule name.
     *
     * @return the rule name
     */
    public String getRuleName() {
        return handler.getRuleName();
    }
}
