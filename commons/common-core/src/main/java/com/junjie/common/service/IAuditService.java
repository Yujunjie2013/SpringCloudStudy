package com.junjie.common.service;


import com.junjie.common.model.Audit;

/**
 * 审计日志接口
 */
public interface IAuditService {
    void save(Audit audit);
}
