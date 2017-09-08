package demo.vo;

import java.util.Date;

public class AdminOperationLog {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 操作员ID
     */
    private Integer adminId;

    /**
     * 审批件的id
     */
    private Long productAuditStatusId;

    /**
     * 认领时间
     */
    private Date claimAt;

    /**
     * 提交时间
     */
    private Date submitAt;

    /**
     * 审核员操作类型（1认领，2通过，3拒绝，4放弃）
     */
    private Byte operation;

    /**
     * 花费时间，处理时间-开始处理时间 
     */
    private Long cost;

    /**
     * 最后更新时间
     */
    private Date updateAt;

    /**
     * 自增id
     * @return id 自增id
     */
    public Long getId() {
        return id;
    }

    /**
     * 自增id
     * @param id 自增id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 操作员ID
     * @return admin_id 操作员ID
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 操作员ID
     * @param adminId 操作员ID
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 审批件的id
     * @return product_audit_status_id 审批件的id
     */
    public Long getProductAuditStatusId() {
        return productAuditStatusId;
    }

    /**
     * 审批件的id
     * @param productAuditStatusId 审批件的id
     */
    public void setProductAuditStatusId(Long productAuditStatusId) {
        this.productAuditStatusId = productAuditStatusId;
    }

    /**
     * 认领时间
     * @return claim_at 认领时间
     */
    public Date getClaimAt() {
        return claimAt;
    }

    /**
     * 认领时间
     * @param claimAt 认领时间
     */
    public void setClaimAt(Date claimAt) {
        this.claimAt = claimAt;
    }

    /**
     * 提交时间
     * @return submit_at 提交时间
     */
    public Date getSubmitAt() {
        return submitAt;
    }

    /**
     * 提交时间
     * @param submitAt 提交时间
     */
    public void setSubmitAt(Date submitAt) {
        this.submitAt = submitAt;
    }

    /**
     * 审核员操作类型（1认领，2通过，3拒绝，4放弃）
     * @return operation 审核员操作类型（1认领，2通过，3拒绝，4放弃）
     */
    public Byte getOperation() {
        return operation;
    }

    /**
     * 审核员操作类型（1认领，2通过，3拒绝，4放弃）
     * @param operation 审核员操作类型（1认领，2通过，3拒绝，4放弃）
     */
    public void setOperation(Byte operation) {
        this.operation = operation;
    }

    /**
     * 花费时间，处理时间-开始处理时间 
     * @return cost 花费时间，处理时间-开始处理时间 
     */
    public Long getCost() {
        return cost;
    }

    /**
     * 花费时间，处理时间-开始处理时间 
     * @param cost 花费时间，处理时间-开始处理时间 
     */
    public void setCost(Long cost) {
        this.cost = cost;
    }

    /**
     * 最后更新时间
     * @return update_at 最后更新时间
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 最后更新时间
     * @param updateAt 最后更新时间
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}