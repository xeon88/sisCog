package DepParser.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Marco Corona on 30/08/2017.
 */
public class ArcEager extends ArcSystem {
    
    public static final int SIZE = 8;

    public enum Type implements operation {

        NOP(-1, "no-op", "") {
            public boolean isAppliable(State state) {
                return false;
            }

            public State apply(State state) {
                return state.cloneState();
            }
        },
        SHIFT(0, "Shift", "") {
            public boolean isAppliable(State state) {
                return true;
            }

            public State apply(State state) {
                State next = state.cloneState();
                Stack<Token> stack = next.getStack();
                LinkedList<Token> buffer = next.getBuffer();
                Token first = buffer.getFirst();
                buffer.removeFirst();
                stack.push(first);
                next.setTopStack(!stack.isEmpty() ? stack.peek() : null);
                next.setFirstBuffer(!buffer.isEmpty() ? buffer.getFirst() : null);
                next.incrementStep();
                return next;
            }
        },

        LEFT_DOBJ(1, "Left", "dobj") {
            public boolean isAppliable(State state) {
                return isLeftAppliable(state);
            }

            public State apply(State state) {
                return applyLeft(state, this.relation);
            }
        },
        LEFT_NSUBJ(2, "Left", "nsubj") {
            public boolean isAppliable(State state) {
                return isLeftAppliable(state);
            }

            public State apply(State state) {
                return applyLeft(state, this.relation);
            }
        },
        LEFT_OTHER(3, "Left", "noname") {
            public boolean isAppliable(State state) {
                return isLeftAppliable(state);
            }

            public State apply(State state) {
                return applyLeft(state, this.relation);
            }
        },
        RIGHT_NSUBJ(4, "Right", "nsubj") {
            public boolean isAppliable(State state) {
                return isRightAppliable(state);
            }

            public State apply(State state) {
                return applyRight(state, this.relation);
            }
        },
        RIGHT_DOBJ(5, "Right", "dobj") {
            public boolean isAppliable(State state) {
                return isRightAppliable(state);
            }

            public State apply(State state) {
                return applyRight(state, this.relation);
            }
        },
        RIGHT_OTHER(6, "Right", "noname") {
            public boolean isAppliable(State state) {
                return isRightAppliable(state);
            }

            public State apply(State state) {
               return applyRight(state,this.relation);
            }
        },
        REDUCE(7, "Reduce", "") {
            public boolean isAppliable(State state) {
                if (!state.getStack().isEmpty()) {
                    Dependency[] arcs = state.getArcs();
                    Token head = state.getTopStack();
                    if ((head.getIndex()==0 && state.isRooted()) ||
                            (head.getIndex()!=0 && arcs[head.getIndex() - 1] != null)) {
                        return true;
                    }
                }
                return false;
            }

            public State apply(State state) {
                State next = state.cloneState();
                Stack<Token> stack = next.getStack();
                LinkedList<Token> buffer = next.getBuffer();
                stack.pop();
                state.setTopStack(!stack.isEmpty() ? stack.peek() : null);
                next.setFirstBuffer(!buffer.isEmpty() ? buffer.getFirst() : null);
                state.incrementStep();
                return next;
            }
        };

        protected int type;
        protected String name;
        protected String relation;

        private Type(int type, String name, String relation) {
            this.type = type;
            this.name = name;
            this.relation = relation;
        }

        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getRelation() {
            return relation;
        }


        protected State applyLeft(State state, String relation) {

            State next = state.cloneState();
            Stack<Token> stack = next.getStack();
            LinkedList<Token> buffer = next.getBuffer();
            Token head = buffer.getFirst();
            Token dependent = stack.pop();
            Dependency dependency = new Dependency(head, dependent, relation);
            if (head.isRoot()) state.setRooted(true);

            next.setArc(dependent.getIndex() - 1, dependency);
            next.setTopStack(!stack.isEmpty() ? stack.peek() : null);
            next.setFirstBuffer(!buffer.isEmpty() ? buffer.getFirst() : null);
            next.incrementStep();
            return next;
        }


        protected State applyRight(State state, String relation) {
            State next = state.cloneState();
            Stack<Token> stack = next.getStack();
            LinkedList<Token> buffer = next.getBuffer();
            Token dependent = buffer.getFirst();
            Token head = stack.peek();
            buffer.removeFirst();
            stack.push(dependent);
            Dependency dependency = new Dependency(head, dependent, relation);
            if (head.isRoot()) state.setRooted(true);
            next.setArc(dependent.getIndex() - 1, dependency);
            next.setTopStack(!stack.isEmpty() ? stack.peek() : null);
            next.setFirstBuffer(!buffer.isEmpty() ? buffer.getFirst() : null);
            next.incrementStep();
            return next;
        }


        public static boolean isLeftAppliable(State state) {
            if (!state.getStack().isEmpty()) {
                Dependency[] arcs = state.getArcs();
                Token head = state.getTopStack();
                if (head.getIndex() != 0 &&
                        arcs[head.getIndex()-1] == null) {
                    return true;
                }
            }
            return false;

        }

        public static boolean isRightAppliable(State state){
            return !state.getStack().isEmpty() ;
        }

        public static boolean isLeftAction(Type action){
            if(action.getType()>=1 && action.getType()<=3){
                return true;
            }
            return false;
        }

        public static boolean isRightAction(Type action){
            if(action.getType()>=4 && action.getType()<=6){
                return true;
            }
            return false;
        }


    }


    public static ArcEager.Type [] getValidAction(State state) {
        ArrayList<ArcEager.Type> appliable = new ArrayList<ArcEager.Type>();
        for(ArcEager.Type action : ArcEager.Type.values()){
            if(action.isAppliable(state)) appliable.add(action);
        }

        return appliable.toArray(new ArcEager.Type[appliable.size()]);
    }

}
