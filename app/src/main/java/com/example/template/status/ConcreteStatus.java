package com.example.template.status;

public abstract class ConcreteStatus implements Status{
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Status other)) return false;

        return this.message().equals(other.message());
    }
}
