package com.file_service.shared.domain;

public interface Auditable<ID> {
    ID getCreatedBy();
    ID getModifiedBy();
    void setModifiedBy(ID userId);
}

