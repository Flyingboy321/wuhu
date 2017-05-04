package com.hhxy.wuhu.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/10.
 */

public class Before {

    /**
     * date : 20161209
     * stories : [{"images":["http://pic2.zhimg.com/d9544c5f9b404fc2d9468322ced44cc9.jpg"],"type":0,"id":9049035,"ga_prefix":"120922","title":"小事 · 差距太大不能不服"},{"images":["http://pic3.zhimg.com/4debfae08b221dd5a175f243849c043e.jpg"],"type":0,"id":9051744,"ga_prefix":"120921","title":"看今敏的动画能感觉到：这家伙是真的热爱电影啊"},{"images":["http://pic1.zhimg.com/632084a73c23dad9ee1c35b4b4176790.jpg"],"type":0,"id":9051666,"ga_prefix":"120920","title":"19 岁的柯洁，拿到了他的第 4 个世界冠军"},{"images":["http://pic2.zhimg.com/3b3ab628a8dbbfb28a49e00ac67232a1.jpg"],"type":0,"id":8810010,"ga_prefix":"120919","title":"光是打麻将，可能还不足以延缓父母认知力的退化"},{"images":["http://pic3.zhimg.com/760360582c1443a683a20e60ebba5266.jpg"],"type":0,"id":9044015,"ga_prefix":"120918","title":"社会对病人的态度，是怎么随着医学进步而改变的？"},{"images":["http://pic3.zhimg.com/60f55ce62abd6eac3a99d6b5156810d6.jpg"],"type":0,"id":9051540,"ga_prefix":"120917","title":"知乎好问题 · 有哪些经典的科幻电影值得推荐？"},{"images":["http://pic1.zhimg.com/971084ccc88ab6390fb81ccb9852baf0.jpg"],"type":0,"id":9051074,"ga_prefix":"120916","title":"关于职场性骚扰，这些事我们需要知道"},{"images":["http://pic4.zhimg.com/d1942528be78399d4333e8e1f85aa967.jpg"],"type":0,"id":9050483,"ga_prefix":"120915","title":"- 为什么要来法国滑雪？ - 因为那有阿尔卑斯山啊"},{"images":["http://pic1.zhimg.com/036f698ae51a792a65dde0178592cf70.jpg"],"type":0,"id":9049435,"ga_prefix":"120914","title":"这一盏灯，点亮要用 530 秒，熄灭要用 459.7 年"},{"images":["http://pic3.zhimg.com/5d4648f7b1d6bcd1251c0dcb2e26000a.jpg"],"type":0,"id":9049310,"ga_prefix":"120913","title":"热闹了一阵之后，VR 行业正在「急速衰退」吗？"},{"images":["http://pic3.zhimg.com/03626950f2e509a58910fc16c8b69e96.jpg"],"type":0,"id":9050329,"ga_prefix":"120912","title":"大误 · 你们快退后，这个怪兽由我来打败"},{"images":["http://pic1.zhimg.com/b6f16a987dc6c8477f087fd2f1dbd100.jpg"],"type":0,"id":9049314,"ga_prefix":"120911","title":"身为一个悬疑作家，我觉得《午夜凶铃》的原著才更精彩"},{"title":"起个大早，把云南的早晨一口吃掉","ga_prefix":"120910","images":["http://pic3.zhimg.com/51d27bbca6aa1dc918a324d4c4edc7ee.jpg"],"multipic":true,"type":0,"id":9045234},{"images":["http://pic2.zhimg.com/3101c35174de27164461264381c18469.jpg"],"type":0,"id":9049395,"ga_prefix":"120909","title":"最近南极冰架发现巨大裂缝，这是气候变暖愈演愈烈的铁证"},{"images":["http://pic4.zhimg.com/48210a027d4fbdc702b341e6e349e1df.jpg"],"type":0,"id":9046253,"ga_prefix":"120908","title":"当父母老了，生活无法自理的残障子女该怎么办？"},{"images":["http://pic2.zhimg.com/2e072bb6d4149ccfeebf2d18ab679c4d.jpg"],"type":0,"id":9049348,"ga_prefix":"120907","title":"税收一「扭曲」，就会调整人们的行为"},{"title":"顺着《西部世界》往前走，发现了一堆影响深远的经典电影","ga_prefix":"120907","images":["http://pic2.zhimg.com/9d771ff399e2b573c8c6688b3cbf0aa1.jpg"],"multipic":true,"type":0,"id":9049332},{"images":["http://pic4.zhimg.com/5c5fb108dbc628aa59e30d20f30153d3.jpg"],"type":0,"id":9047769,"ga_prefix":"120907","title":"「怀孕六周禁止堕胎」的法案，可能产生什么影响？"},{"images":["http://pic4.zhimg.com/69e673389de48a8d7a12f2f12220a237.jpg"],"type":0,"id":9049387,"ga_prefix":"120907","title":"读读日报 24 小时热门 TOP 5 · 一个父亲的网络救女史"},{"images":["http://pic3.zhimg.com/d6f6d1ce220d1c7463d54bb689c58ea2.jpg"],"type":0,"id":9048302,"ga_prefix":"120906","title":"瞎扯 · 如何正确地吐槽"},{"images":["http://pic2.zhimg.com/a2b83a495da12c49a5d4de133bdf35a5.jpg"],"type":0,"id":9048051,"ga_prefix":"120906","title":"这里是广告 · 来！我有一个过亿的项目跟你谈一谈！"}]
     */

    private String date;
    private List<StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "Before{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }
}


