package demo.java.util;

import java.util.UUID;

/**
 * UUID 是 通用唯一识别码（Universally Unique Identifier）的缩写，是一种软件建构的标准，亦为开放软件基金会组织在分布式计算环境领域的一部分。
 * 其目的，是让分布式系统中的所有元素，都能有唯一的辨识信息，而不需要通过中央控制端来做辨识信息的指定。如此一来，每个人都可以创建不与其它人冲突的UUID。
 * <p>
 * UUID由以下几部分的组合：
 * <li>（1）当前日期和时间，UUID的第一个部分与时间有关，如果你在生成一个UUID之后，过几秒又生成一个UUID，则第一个部分不同，其余相同。
 * <li>（2）时钟序列。
 * <li>（3）全局唯一的IEEE机器识别号，如果有网卡，从网卡MAC地址获得，没有网卡以其他方式获得。
 * <p>
 * UUID是由一组32位数的16进制数字所构成，是故UUID理论上的总数为16^32=2^128，约等于3.4 x 10^38。也就是说若每纳秒产生1兆个UUID，要花100亿年才会将所有UUID用完。
 * UUID的标准型式包含32个16进制数字，以连字号分为五段，形式为8-4-4-4-12的32个字符。示例： 550e8400-e29b-41d4-a716-446655440000
 * 标准的UUID格式为：xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (8-4-4-4-12)。 其中每个 x 是 0-9 或 a-f 范围内的一个十六进制的数字。
 * 
 */
public class UUIDDemo {

    public static void main(String[] args) {
        while (true) {
            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);
            System.out.println(uuid.toString().replaceAll("-", ""));
            System.out.println(uuid.toString().length());
        }

    }

}
