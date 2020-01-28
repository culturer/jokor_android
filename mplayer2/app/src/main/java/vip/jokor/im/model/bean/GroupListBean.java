package vip.jokor.im.model.bean;

import java.util.List;

public class GroupListBean {


    /**
     * groups : [{"Id":1,"Sort":0,"Msg":"","Belong":1,"Group":{"Id":11,"Icon":"","Name":"测试！","Sign":"","Msg":"","Pwd":"","City":0,"IsCity":false,"Administers":"","Belong":1,"MaxCount":100,"IsPay":false,"PayNum":0,"Pri":0,"Grad":0,"CreateTime":"2019-11-14 14:22:30"}},{"Id":2,"Sort":0,"Msg":"","Belong":1,"Group":{"Id":12,"Icon":"","Name":"测试！","Sign":"","Msg":"","Pwd":"","City":0,"IsCity":false,"Administers":"","Belong":1,"MaxCount":100,"IsPay":false,"PayNum":0,"Pri":0,"Grad":0,"CreateTime":"2019-11-14 14:23:54"}},{"Id":3,"Sort":0,"Msg":"","Belong":1,"Group":{"Id":13,"Icon":"","Name":"测试群","Sign":"","Msg":"","Pwd":"","City":0,"IsCity":false,"Administers":"","Belong":1,"MaxCount":100,"IsPay":false,"PayNum":0,"Pri":0,"Grad":0,"CreateTime":"2019-11-14 14:39:18"}},{"Id":4,"Sort":0,"Msg":"","Belong":1,"Group":{"Id":14,"Icon":"","Name":"测试群聊","Sign":"","Msg":"","Pwd":"","City":0,"IsCity":false,"Administers":"","Belong":1,"MaxCount":100,"IsPay":false,"PayNum":0,"Pri":0,"Grad":0,"CreateTime":"2019-11-14 14:59:43"}}]
     * status : 200
     * time : 2019-11-14 23:55:37
     */

    private int status;
    private String time;
    private List<GroupsBean> groups;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<GroupsBean> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsBean> groups) {
        this.groups = groups;
    }

    public static class GroupsBean {
        /**
         * Id : 1
         * Sort : 0
         * Msg :
         * Belong : 1
         * Group : {"Id":11,"Icon":"","Name":"测试！","Sign":"","Msg":"","Pwd":"","City":0,"IsCity":false,"Administers":"","Belong":1,"MaxCount":100,"IsPay":false,"PayNum":0,"Pri":0,"Grad":0,"CreateTime":"2019-11-14 14:22:30"}
         */

        private long Id;
        private int Sort;
        private String Msg;
        private long Belong;
        private GroupBean Group;

        public long getId() {
            return Id;
        }

        public void setId(long Id) {
            this.Id = Id;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }

        public long getBelong() {
            return Belong;
        }

        public void setBelong(long Belong) {
            this.Belong = Belong;
        }

        public GroupBean getGroup() {
            return Group;
        }

        public void setGroup(GroupBean Group) {
            this.Group = Group;
        }

    }
}
