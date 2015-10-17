package com.vackosar.searchbasedlauncher.entity;

public abstract class Action {

    public abstract String getId();
    public abstract void act();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        return getId().equals(action.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
