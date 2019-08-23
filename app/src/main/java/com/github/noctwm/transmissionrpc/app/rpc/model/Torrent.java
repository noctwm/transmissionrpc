package com.github.noctwm.transmissionrpc.app.rpc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Torrent {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("totalSize")
    @Expose
    private long totalSize;

    private int status;

    private long sizeWhenDone;

    private long leftUntilDone;

    private long rateDownload;

    private long rateUpload;

    private float percentDone;

    private long downloadedEver;

    private long uploadedEver;

    private int eta;

    private String downloadDir;

    private String creator;

    private String comment;

    private long dateCreated;

    private long activityDate;

    private long addedDate;

    private long doneDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getRateUpload() {
        return rateUpload;
    }

    public void setRateUpload(long rateUpload) {
        this.rateUpload = rateUpload;
    }

    public long getRateDownload() {
        return rateDownload;
    }

    public void setRateDownload(long rateDownload) {
        this.rateDownload = rateDownload;
    }

    public float getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(float percentDone) {
        this.percentDone = percentDone;
    }

    public long getUploadedEver() {
        return uploadedEver;
    }

    public void setUploadedEver(long uploadedEver) {
        this.uploadedEver = uploadedEver;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public long getSizeWhenDone() {
        return sizeWhenDone;
    }

    public void setSizeWhenDone(long sizeWhenDone) {
        this.sizeWhenDone = sizeWhenDone;
    }

    public long getLeftUntilDone() {
        return leftUntilDone;
    }

    public void setLeftUntilDone(long leftUntilDone) {
        this.leftUntilDone = leftUntilDone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(long activityDate) {
        this.activityDate = activityDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public long getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(long doneDate) {
        this.doneDate = doneDate;
    }

    public long getDownloadedEver() {
        return downloadedEver;
    }

    public void setDownloadedEver(long downloadedEver) {
        this.downloadedEver = downloadedEver;
    }
}
