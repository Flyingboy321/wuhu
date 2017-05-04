package com.hhxy.wuhu.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9.
 */

//我们把这个类抽取出来
public class StoriesBean implements Serializable {

        @Override
        public String toString() {
            return "StoriesBean{" +
                    "ga_prefix='" + ga_prefix + '\'' +
                    ", type=" + type +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", multipic=" + multipic +
                    ", images=" + images +
                    '}';
        }

        /**
         * images : ["http://pic1.zhimg.com/971084ccc88ab6390fb81ccb9852baf0.jpg"]
         * type : 0
         * id : 9051074
         * ga_prefix : 120916
         * title : 关于职场性骚扰，这些事我们需要知道
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }


}
