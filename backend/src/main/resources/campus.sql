-- AI智能校园助手平台 数据库初始化脚本

CREATE DATABASE IF NOT EXISTS campus_assistant DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_assistant;

-- 用户表
DROP TABLE IF EXISTS study_plan;
DROP TABLE IF EXISTS chat_message;
DROP TABLE IF EXISTS classroom_booking;
DROP TABLE IF EXISTS classroom;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255) DEFAULT '/avatar.png',
    role VARCHAR(20) DEFAULT 'student',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '用户表';

INSERT INTO user (username, password, nickname, role) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin'),
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'student');

-- 校园新闻表
CREATE TABLE news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    cover_image VARCHAR(500),
    source VARCHAR(100) DEFAULT '校园新闻中心',
    category VARCHAR(50),
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    view_count INT DEFAULT 0
) COMMENT '校园新闻表';

INSERT INTO news (title, content, category, source, view_count) VALUES
('我校在2024年全国大学生数学建模竞赛中荣获一等奖', '<p>近日，2024年全国大学生数学建模竞赛成绩揭晓，我校代表队凭借出色的表现荣获全国一等奖。本次竞赛吸引了来自全国各地的数千支队伍参赛，我校学子在激烈的竞争中脱颖而出。</p><p>据悉，本次获奖团队由来自计算机学院、数学学院的三名学生组成，在指导教师的带领下，经过三天三夜的紧张奋战，最终提交了高质量的论文。</p>', '学术动态', '教务处', 1280),
('关于2024年秋季学期选课通知', '<p>各位同学：</p><p>2024年秋季学期选课工作即将开始，现将有关事项通知如下：</p><p>一、选课时间：2024年7月15日-7月20日</p><p>二、选课方式：通过教务系统在线选课</p><p>三、注意事项：请同学们提前查看培养方案，合理规划课程。</p>', '通知公告', '教务处', 856),
('校园文化节系列活动精彩纷呈', '<p>为期一周的校园文化节于本周一正式拉开帷幕。本次活动涵盖文艺演出、学术讲座、体育竞赛等多个板块，吸引了全校师生的广泛参与。</p><p>文化节期间，学校还邀请了多位知名学者来校进行学术交流，为同学们提供了宝贵的学习机会。</p>', '校园生活', '学生处', 632),
('图书馆新增AI学习空间，配备先进设备', '<p>为满足广大师生的学习需求，图书馆近日完成了AI学习空间的改造工程。新增的学习空间配备了高性能计算机、大屏幕显示器以及各类AI开发工具。</p><p>该空间面向全校师生免费开放，开放时间为每日8:00-22:00。</p>', '设施建设', '图书馆', 945),
('关于开展大学生创新创业项目申报的通知', '<p>各学院：</p><p>2024年度大学生创新创业训练计划项目申报工作现已启动。请有意向的同学于8月1日前提交申报材料。</p><p>项目类别包括创新训练项目、创业训练项目和创业实践项目。</p>', '通知公告', '创新创业学院', 723);

-- 课程表
CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    teacher VARCHAR(50),
    week_day INT COMMENT '1-7 周一到周日',
    start_slot INT COMMENT '开始节次1-12',
    end_slot INT COMMENT '结束节次1-12',
    classroom VARCHAR(100),
    credits DECIMAL(3,1) DEFAULT 3.0,
    capacity INT DEFAULT 60,
    semester VARCHAR(20) DEFAULT '2024-2025-1',
    description VARCHAR(500)
) COMMENT '课程表';

INSERT INTO course (name, teacher, week_day, start_slot, end_slot, classroom, credits, capacity, description) VALUES
('高等数学A(上)', '王明远', 1, 1, 2, '教一楼101', 5.0, 120, '高等数学是理工科各专业的重要基础课程'),
('数据结构与算法', '李建国', 1, 3, 4, '教三楼205', 4.0, 80, '学习常见数据结构和基本算法'),
('大学英语(三)', '张丽华', 2, 1, 2, '教二楼302', 3.0, 60, '提高英语综合应用能力'),
('面向对象程序设计', '陈志强', 2, 3, 5, '教三楼301', 3.0, 60, '学习Java面向对象编程思想'),
('数据库原理', '赵晓明', 3, 1, 2, '教三楼201', 3.0, 80, '学习关系型数据库理论和SQL'),
('计算机网络', '刘伟', 3, 3, 4, '教三楼202', 3.0, 70, '学习计算机网络体系结构'),
('人工智能导论', '孙教授', 4, 1, 2, '教一楼201', 3.0, 80, '介绍人工智能基本概念和应用'),
('操作系统', '周文博', 4, 3, 5, '教三楼203', 4.0, 70, '学习操作系统的原理和设计'),
('软件工程', '吴老师', 5, 1, 2, '教二楼401', 3.0, 60, '学习软件开发的工程化方法'),
('线性代数', '王明远', 5, 3, 4, '教一楼101', 3.0, 100, '学习矩阵理论和线性空间');

-- 教室表
CREATE TABLE classroom (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    building VARCHAR(100),
    floor INT,
    capacity INT DEFAULT 60,
    has_projector TINYINT(1) DEFAULT 1,
    status VARCHAR(20) DEFAULT 'available'
) COMMENT '教室表';

INSERT INTO classroom (name, building, floor, capacity, has_projector) VALUES
('101', '教一楼', 1, 120, 1),
('102', '教一楼', 1, 80, 1),
('201', '教一楼', 2, 80, 1),
('202', '教一楼', 2, 60, 0),
('301', '教二楼', 3, 60, 1),
('302', '教二楼', 3, 60, 1),
('401', '教二楼', 4, 60, 1),
('402', '教二楼', 4, 50, 0),
('201', '教三楼', 2, 80, 1),
('202', '教三楼', 2, 70, 1),
('203', '教三楼', 2, 70, 0),
('301', '教三楼', 3, 60, 1),
('302', '教三楼', 3, 50, 0);

-- 教室占用表
CREATE TABLE classroom_booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    classroom_id BIGINT NOT NULL,
    week_day INT COMMENT '1-7',
    start_slot INT,
    end_slot INT,
    course_name VARCHAR(100),
    semester VARCHAR(20) DEFAULT '2024-2025-1'
) COMMENT '教室占用表';

INSERT INTO classroom_booking (classroom_id, week_day, start_slot, end_slot, course_name) VALUES
(1, 1, 1, 2, '高等数学A(上)'),
(1, 5, 3, 4, '线性代数'),
(2, 1, 3, 4, '数字电路'),
(3, 3, 1, 2, '数据库原理'),
(4, 3, 3, 4, '计算机网络'),
(5, 2, 1, 2, '大学英语(三)'),
(6, 2, 3, 5, '面向对象程序设计'),
(7, 4, 1, 2, '人工智能导论'),
(8, 4, 3, 5, '操作系统'),
(9, 5, 1, 2, '软件工程');

-- AI对话消息表
CREATE TABLE chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL COMMENT 'user/assistant',
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT 'AI对话记录表';

-- 学习计划表
CREATE TABLE study_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    status VARCHAR(20) DEFAULT 'active' COMMENT 'active/completed/cancelled',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '学习计划表';
