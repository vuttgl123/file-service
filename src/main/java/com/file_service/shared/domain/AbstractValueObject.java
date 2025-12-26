package com.file_service.shared.domain;

public abstract class AbstractValueObject implements ValueObject {
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return equalsByValue((AbstractValueObject) obj);
    }

    protected abstract boolean equalsByValue(AbstractValueObject other);

    @Override
    public final int hashCode() {
        return hashCodeByValue();
    }

    protected abstract int hashCodeByValue();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
