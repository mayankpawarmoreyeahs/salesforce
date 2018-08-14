package demo.recycle.com.salforceapp.pojo;

public class Taskpojo {
    private String TaskName;
    private String Id;
    private String eventDate;
    private String Description;
    private String Accountid;
    private String priority;
    private String status;
    private String accountid;
    private String contactid;
    private String ownerid;

    public static final String TABLE_NAME = "tasks";

    public static final String TASK_ID = "id";
    public static final String TASK_Description = "Description";
    public static final String TASK_Status = "Status";
    public static final String TASK_ActivityDate = "ActivityDate";
    public static final String TASK_Priority = "Priority";
    public static final String TASK_Subject = "Subject";
    public static final String TASK_WhoId = "WhoId";
    public static final String TASK_WhatId = "WhatId";
    public static final String TASK_OwnerId = "OwnerId";




    public static final String TABLE_NAME1 = "drafts";

    public static final String DRAFT_ID = "id";
    public static final String DRAFT_Description = "Description";
    public static final String DRAFT_Status = "Status";
    public static final String DRAFT_ActivityDate = "ActivityDate";
    public static final String DRAFT_Priority = "Priority";
    public static final String DRAFT_Subject = "Subject";
    public static final String DRAFT_WhoId = "WhoId";
    public static final String DRAFT_WhatId = "WhatId";
    public static final String DRAFT_OwnerId = "OwnerId";







  //  WhatId,OwnerId,Description,Status,ActivityDate,Priority,Subject,WhoId



    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TASK_WhatId + " TEXT,"
                    + TASK_OwnerId + " TEXT,"
                    + TASK_Description + " TEXT,"
                    + TASK_Status + " TEXT,"
                    + TASK_ActivityDate + " TEXT,"
                    + TASK_Priority + " TEXT,"
                    + TASK_Subject + " TEXT,"
                    + TASK_WhoId + " TEXT"
                    + ")";

    public static final String CREATE_TABLE1 =
            "CREATE TABLE " + TABLE_NAME1 + "("
                    + DRAFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DRAFT_WhatId + " TEXT,"
                    + DRAFT_OwnerId + " TEXT,"
                    + DRAFT_Description + " TEXT,"
                    + DRAFT_Status + " TEXT,"
                    + DRAFT_ActivityDate + " TEXT,"
                    + DRAFT_Priority + " TEXT,"
                    + DRAFT_Subject + " TEXT,"
                    + DRAFT_WhoId + " TEXT"
                    + ")";




    public Taskpojo()
    {
    }


    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAccountid() {
        return Accountid;
    }

    public Taskpojo(String TaskName, String id, String eventDate, String description, String accountid, String contactid) {
        this.TaskName = TaskName;
        Id = id;
        this.eventDate = eventDate;
        Description = description;
        Accountid = accountid;
        Contactid = contactid;
    }

    public void setAccountid(String accountid) {

        Accountid = accountid;
    }

    public String getContactid() {
        return Contactid;
    }

    public void setContactid(String contactid) {
        Contactid = contactid;
    }

    private String Contactid;
    public Taskpojo(String Id, String TaskName, String eventDate ) {
        this.TaskName = TaskName;
        this.Id = Id;
        this.eventDate = eventDate;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String TaskName) {
        this.TaskName = TaskName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}