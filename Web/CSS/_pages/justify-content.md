```CSS
/* Positional alignment */
justify-content: center;     /* 居中排列。每行第一个元素到行首的距离将与每行最后一个元素到行尾的距离相同 */
justify-content: start;      /* 从行首开始排列。每行第一个元素与行首对齐，所有后续的元素与前一个对齐 */
justify-content: end;        /* 右对齐 Pack items from the end */
justify-content: flex-start; /* 从行首开始排列。每行第一个弹性元素与行首对齐，后续的弹性元素与前一个对齐 */
justify-content: flex-end;   /* 从行尾开始排列。每行最后一个弹性元素与行尾对齐，其他元素将与后一个对齐 */
justify-content: left;       /* 伸缩元素一个个在对齐容器得左边缘，属性轴与内联轴不平行时类似于 start */
justify-content: right;      /* 元素以容器右边缘为基准一个个对齐，属性轴与内联轴不平行时类似于 end  */

/* Baseline alignment */
justify-content: baseline;
justify-content: first baseline;
justify-content: last baseline;

/* Distributed alignment */
justify-content: space-between;  /* 相邻元素间距离相同，每行第一个元素与行首对齐，每行最后一个元素与行尾对齐 */
justify-content: space-around;   /* 相邻元素间距离相同，每行第一个元素到行首的距离和每行最后一个元素到行尾的距离将会是相邻元素之间距离的一半 */
justify-content: space-evenly;   /*  相邻元素间距离，每行第一个元素到行首的距离和每行最后一个元素到行尾的距离都相同 */
justify-content: stretch;        /* 均匀排列每个元素 auto-sized 的元素会被拉伸以适应容器的大小 */

/* Overflow alignment */
justify-content: safe center;    /* safe 与对齐关键字一起使用，如果选定的关键字会导致元素溢出容器造成数据丢失，那么将会使用 start 代替它 */
justify-content: unsafe center;

/* Global values */
justify-content: inherit;
justify-content: initial;
justify-content: unset;
```