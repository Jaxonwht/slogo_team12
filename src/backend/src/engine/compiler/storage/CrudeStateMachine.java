package engine.compiler.storage;

import engine.compiler.slogoast.Expression;
import engine.errors.InterpretationException;

import java.util.*;

/**
 * This StateMachine records variables that the parser can use. For example, it records "x = 3".
 *
 * @author Haotian Wang
 */
public class CrudeStateMachine implements StateMachine {
    private Map<String, String> typeMap;
    private Map<String, Double> doubleMap;
    private Map<String, Integer> integerMap;
    private Map<String, String> stringMap;
    private Map<String, Expression> functionMap;

    private Map<String, Object> aggregateMap;

    private List<StateMachineObserver> observers;

    public CrudeStateMachine() {
        typeMap = new HashMap<>();
        doubleMap = new HashMap<>();
        integerMap = new HashMap<>();
        stringMap = new HashMap<>();
        functionMap = new HashMap<>();
        aggregateMap = new HashMap<>();

        observers = new ArrayList<>();
    }

    public void register(StateMachineObserver observer) { observers.add(observer); }
    public void pushAlarm() { observers.forEach(StateMachineObserver::notifyListener);}

    /**
     * Set a double value for a variable.
     *
     * @param key
     * @param value
     */
    public void setDouble(String key, double value) {
        if (typeMap.containsKey(key)) {
            String type = typeMap.get(key);
            if (type.equals("Integer")) {
                integerMap.remove(key);
            } else if (type.equals("String")) {
                stringMap.remove(key);
            } else if (type.equals("Function")) {
                functionMap.remove(key);
            }
        }
        doubleMap.put(key, value);
        aggregateMap.put(key, value);
        typeMap.put(key, "Double");
        pushAlarm();
    }

    /**
     * Set an int value for a variable.
     *
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        if (typeMap.containsKey(key)) {
            String type = typeMap.get(key);
            if (type.equals("Double")) {
                doubleMap.remove(key);
            } else if (type.equals("String")) {
                stringMap.remove(key);
            } else if (type.equals("Function")) {
                functionMap.remove(key);
            }
        }
        integerMap.put(key, value);
        aggregateMap.put(key, value);
        typeMap.put(key, "Integer");
        pushAlarm();
    }

    /**
     * Set a String value for a variable.
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        if (typeMap.containsKey(key)) {
            String type = typeMap.get(key);
            if (type.equals("Double")) {
                doubleMap.remove(key);
            } else if (type.equals("Integer")) {
                integerMap.remove(key);
            } else if (type.equals("Function")) {
                functionMap.remove(key);
            }
        }
        stringMap.put(key, value);
        aggregateMap.put(key, value);
        typeMap.put(key, "Integer");
        pushAlarm();
    }

    /**
     * Get the type of the variable, either a double, an integer or a function.
     *
     * @param key
     * @return A String representation of the type of the variable.
     */
    @Override
    public String getVariableType(String key) {
        return typeMap.get(key);
    }

    /**
     * Remove the key entry from the map.
     *
     * @param key
     */
    public void removeVariable(String key) throws InterpretationException {
        if (!typeMap.containsKey(key)) {
            throw new InterpretationException(String.format("The variable %s is not defined, therefore cannot be removed", key));
        }
        String type = typeMap.get(key);
        if (type.equals("Double")) {
            doubleMap.remove(key);
        } else if (type.equals("Integer")) {
            integerMap.remove(key);
        } else if (type.equals("Function")) {
            functionMap.remove(key);
        } else if (type.equals("String")) {
            stringMap.remove(key);
        } aggregateMap.remove(key);
        pushAlarm();
    }

    /**
     * Clear all state variables in the state machine.
     */
    public void resetState() {
        typeMap.clear();
        integerMap.clear();
        doubleMap.clear();
        functionMap.clear();
        stringMap.clear();
        aggregateMap.clear();
        pushAlarm();
    }

    @Override
    public Map<String, Object> listOfVariables() { return Collections.unmodifiableMap(aggregateMap); }

    /**
     * Present the internal storage of the StateMachine in a list format, separated by newline.
     *
     * @return A String representation of the StateMachine.
     */
    @Override
    public String toString() {
        String ans = "";
        for (Map.Entry entry : integerMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        for (Map.Entry entry : doubleMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        for (Map.Entry entry : stringMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        for (Map.Entry entry : functionMap.entrySet()) {
            ans += entry.getKey() + " = " + entry.getValue() + "\n";
        }
        return ans;
    }
}