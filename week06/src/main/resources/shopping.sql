create table order_cart
(
    id              bigint                              not null comment '主键'
        primary key,
    user_info_id    bigint                              not null comment '用户id',
    product_info_id bigint                              not null comment '商品id',
    product_cnt     int       default 1                 not null comment '商品数量',
    price           decimal                             not null comment '商品价格',
    gmt_create      timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify      timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
) comment '购物车';


create table order_info
(
    id                 bigint                              not null comment '主键'
        primary key,
    order_no           varchar(32)                         not null comment '订单编号',
    user_info_id       bigint                              not null comment '用户id',
    booking_time       timestamp default CURRENT_TIMESTAMP not null comment '下单时间',
    pay_time           timestamp                           null comment '支付时间',
    shipping_time      timestamp                           null comment '发货时间',
    receive_time       timestamp                           null comment '签收时间',
    order_status       tinyint                             not null comment '订单状态 0-待支付 1-待发货 2-待收货 3-已完成',
    order_amount       decimal                             not null comment '订单金额',
    discount_amount    decimal   default 0                 not null comment '优惠金额',
    shipping_amount    decimal                             not null comment '运费',
    payment_amount     bigint                              not null comment '支付金额',
    shipping_user      varchar(50)                         not null comment '收货人姓名',
    province           smallint                            not null comment '省',
    city               smallint                            not null comment '市',
    district           smallint                            not null comment '区',
    address            varchar(100)                        not null comment '详细地址',
    payment_method     tinyint                             not null comment '支付方式',
    invoice_title      varchar(50)                         null comment '发票抬头',
    order_point        int                                 not null comment '订单积分',
    shipping_comp_name varchar(30)                         null comment '物流公司',
    shipping_no        varchar(20)                         null comment '物流单号',
    gmt_create         timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify         timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间',
    constraint uk_order_info_order_no
        unique (order_no)
)
    comment '订单主表';

create table order_item
(
    id                         bigint                              not null comment '主键'
        primary key,
    order_info_id              bigint                              not null comment '订单id',
    product_info_id            bigint                              not null comment '商品id',
    product_name               varchar(50)                         not null comment '商品名称',
    product_cnt                int       default 1                 not null comment '购买商品数量',
    unit_price                 decimal                             not null comment '购买商品单价',
    discount                   decimal                             not null comment '优惠分摊金额',
    product_storehouse_info_id bigint                              not null comment '仓库id',
    price                      decimal                             not null comment '商品总价',
    gmt_create                 timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify                 timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '订单条目信息表';


create table product_brand
(
    id         bigint                              not null comment '主键'
        primary key,
    brand_name varchar(50)                         not null comment '品牌名称',
    brand_desc varchar(512)                        null comment '品牌描述',
    brand_logo varchar(128)                        null comment '品牌logo的url',
    telephone  varchar(20)                         null comment '品牌联系电话',
    gmt_create timestamp default CURRENT_TIMESTAMP not null,
    gmt_modify timestamp default CURRENT_TIMESTAMP not null
)
    comment '品牌信息表';

create table product_category
(
    id              bigint                              not null comment '主键'
        primary key,
    category_code   varchar(10)                         not null comment '类别编码',
    category_name   varchar(20)                         not null comment '类别名称',
    parent_id       bigint                              not null comment '父类别id',
    category_status tinyint                             not null comment '类别状态',
    gmt_create      timestamp default CURRENT_TIMESTAMP not null,
    gmt_modify      timestamp default CURRENT_TIMESTAMP null
)
    comment '商品类别信息表';


create table product_comment
(
    id              bigint                              not null comment '主键'
        primary key,
    product_info_id bigint                              not null comment '商品id',
    user_info_id    bigint                              not null comment '用户id',
    order_info_id   bigint                              not null comment '订单id',
    title           varchar(50)                         not null comment '标题',
    content         varchar(500)                        null comment '评论内容',
    audit_time      timestamp                           not null comment '评论时间',
    audit_status    tinyint                             not null comment '审核状态 0-未审核 1-已审核',
    anonymous       tinyint                             not null comment '是否匿名 0-否 1-是',
    gmt_create      timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify      timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '商品评论信息表';

create table product_info
(
    id                    bigint                              not null comment '主键'
        primary key,
    product_code          char(16)                            not null comment '商品编码',
    product_name          varchar(30)                         not null comment '商品名称',
    product_brand_id      bigint                              not null comment '品牌id',
    product_supplier_id   bigint                              not null comment '供应商id',
    primary_category_id   bigint                              null comment '一级分类',
    secondary_category_id bigint                              null comment '二级分类',
    third_category_id     bigint                              null comment '三级分类',
    price                 decimal                             not null comment '商品销售价格',
    cost                  decimal                             not null comment '成本价格',
    publish_status        tinyint                             not null comment '上架状态 0-下架 1-上架',
    product_desc          text                                null comment '商品描述',
    color                 varchar(6)                          null comment '商品颜色',
    size                  varchar(5)                          null comment '商品尺码',
    production_date       datetime                            not null comment '生产日期',
    shelf_life            int                                 null comment '保质期',
    gmt_create            timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify            timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '商品信息表';


create table product_inventory
(
    id                    bigint                              not null comment '主键'
        primary key,
    product_info_id       bigint                              not null comment '商品id',
    product_storehouse_id bigint                              not null comment '仓库id',
    current_count         int                                 not null comment '当前数量',
    lock_count            int                                 not null comment '被占用数量',
    gmt_create            timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify            timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '商品库存表';

create table product_pic
(
    id              bigint                              not null comment '主键'
        primary key,
    product_info_id bigint                              not null comment '商品id',
    pic_url         varchar(200)                        not null comment '图片url',
    pic_desc        varchar(50)                         null comment '图片描述',
    is_master       tinyint                             not null comment '是否主图 0-否 1-是',
    pic_order       tinyint                             not null comment '图片排序',
    pic_status      tinyint                             not null comment '是否有效 0-无效 1-有效',
    gmt_create      timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify      timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '商品图片信息表';

create table product_storehouse
(
    id                bigint                              not null comment '主键'
        primary key,
    storehouse_code   char(8)                             not null comment '仓库编码',
    storehouse_name   varchar(50)                         not null comment '仓库名称',
    phone             varchar(20)                         not null comment '仓库电话',
    address           varchar(100)                        not null comment '地址',
    storehouse_status tinyint                             not null comment '仓库状态 0-禁用 1-启用',
    contacts          varchar(50)                         not null comment '联系人',
    province          smallint                            not null comment '省',
    city              smallint                            null comment '市',
    district          smallint                            not null comment '区',
    gmt_create        timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify        timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '仓库信息表';


create table product_supplier
(
    id              bigint                              not null comment '主键'
        primary key,
    supplier_code   varchar(8)                          not null comment '供应商编码',
    supplier_name   varchar(50)                         not null comment '供应商名称',
    supplier_type   tinyint                             not null comment '供应商类型 1-自营 2-第三方',
    contacts        varchar(50)                         not null comment '供应商联系人',
    telephone       varchar(20)                         not null comment '联系电话',
    address         varchar(200)                        not null comment '地址',
    bank_name       varchar(100)                        not null comment '开户行名称',
    bank_account    varchar(20)                         not null comment '开户行账号',
    supplier_status tinyint                             not null comment '状态 0-禁用 1-启用',
    gmt_create      timestamp default CURRENT_TIMESTAMP not null,
    gmt_modify      timestamp default CURRENT_TIMESTAMP not null
)
    comment '供应商信息表';



create table user_info
(
    id            bigint                              not null comment '主键'
        primary key,
    user_login_id bigint                              not null comment '用户登录信息表id',
    user_name     varchar(64)                         not null comment '姓名',
    gener         char                                null comment '性别 F-女 M-男',
    id_type       tinyint                             not null comment '证件类型 1-身份证 2-护照 3-其他',
    id_no         varchar(20)                         not null comment '证件号码',
    mobile        varchar(16)                         null comment '手机号码',
    email         varchar(20)                         null comment '邮箱',
    birthday      datetime                            null comment '生日',
    user_level    tinyint   default 1                 not null comment '会员等级 1-普通会员 2-银卡会员 3-金卡会员 4-铂金卡会员',
    user_point    int       default 0                 not null comment '积分',
    gmt_create    timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify    timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '用户信息表';


create table user_level_info
(
    id         bigint                              not null comment '主键'
        primary key,
    level_name varchar(15)                         not null comment '等级名称',
    min_point  int       default 0                 not null comment '级别最低积分',
    max_point  int       default 0                 not null comment '级别最高分',
    gmt_create timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '用户会员等级信息表';

create table user_login
(
    id         bigint                              not null comment '主键'
        primary key,
    login_name varchar(20)                         not null comment '用户名',
    pwd        char(32)                            not null comment '密码',
    status     tinyint   default 1                 not null comment '用户状态: 1-正常 0-无效',
    gmt_create timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modify timestamp default CURRENT_TIMESTAMP not null comment '最后修改时间'
)
    comment '用户登录信息表';


