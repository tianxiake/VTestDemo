package com.snail.vds.entity;

import java.io.Serializable;

/**
 * @author yongjie created on 2019-08-04.
 */
public class VDSBlockMarket implements Serializable {


    /**
     * code : 1
     * msg :
     * time : 1564899839
     * data : {"list":{"vollar_CNY":{"last":"20.73330000","change":"-4.2775","high":"21.69990000","low":"19.89200000","vol":"110403.81960000","time":1564899839,"name":"CNY"},"vollar_USDT":{"last":"3.00100300","change":"-1.2828","high":"3.15800000","low":"2.97090500","vol":"3509.15000000","time":1564899839,"name":"USDT"},"vollar_BTC":{"last":"0.00029300","change":"11.8320","high":"0.00029300","low":"0.00026200","vol":"34.10000000","time":1564899839,"name":"BTC"}},"block":{"height":223599}}
     */

    private int code;
    private String msg;
    private String time;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * list : {"vollar_CNY":{"last":"20.73330000","change":"-4.2775","high":"21.69990000","low":"19.89200000","vol":"110403.81960000","time":1564899839,"name":"CNY"},"vollar_USDT":{"last":"3.00100300","change":"-1.2828","high":"3.15800000","low":"2.97090500","vol":"3509.15000000","time":1564899839,"name":"USDT"},"vollar_BTC":{"last":"0.00029300","change":"11.8320","high":"0.00029300","low":"0.00026200","vol":"34.10000000","time":1564899839,"name":"BTC"}}
         * block : {"height":223599}
         */

        private ListBean list;
        private BlockBean block;

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public BlockBean getBlock() {
            return block;
        }

        public void setBlock(BlockBean block) {
            this.block = block;
        }

        public static class ListBean {
            /**
             * vollar_CNY : {"last":"20.73330000","change":"-4.2775","high":"21.69990000","low":"19.89200000","vol":"110403.81960000","time":1564899839,"name":"CNY"}
             * vollar_USDT : {"last":"3.00100300","change":"-1.2828","high":"3.15800000","low":"2.97090500","vol":"3509.15000000","time":1564899839,"name":"USDT"}
             * vollar_BTC : {"last":"0.00029300","change":"11.8320","high":"0.00029300","low":"0.00026200","vol":"34.10000000","time":1564899839,"name":"BTC"}
             */

            private VollarCNYBean vollar_CNY;
            private VollarUSDTBean vollar_USDT;
            private VollarBTCBean vollar_BTC;

            public VollarCNYBean getVollar_CNY() {
                return vollar_CNY;
            }

            public void setVollar_CNY(VollarCNYBean vollar_CNY) {
                this.vollar_CNY = vollar_CNY;
            }

            public VollarUSDTBean getVollar_USDT() {
                return vollar_USDT;
            }

            public void setVollar_USDT(VollarUSDTBean vollar_USDT) {
                this.vollar_USDT = vollar_USDT;
            }

            public VollarBTCBean getVollar_BTC() {
                return vollar_BTC;
            }

            public void setVollar_BTC(VollarBTCBean vollar_BTC) {
                this.vollar_BTC = vollar_BTC;
            }

            public static class VollarCNYBean {
                /**
                 * last : 20.73330000
                 * change : -4.2775
                 * high : 21.69990000
                 * low : 19.89200000
                 * vol : 110403.81960000
                 * time : 1564899839
                 * name : CNY
                 */

                private String last;
                private String change;
                private String high;
                private String low;
                private String vol;
                private int time;
                private String name;

                public String getLast() {
                    return last;
                }

                public void setLast(String last) {
                    this.last = last;
                }

                public String getChange() {
                    return change;
                }

                public void setChange(String change) {
                    this.change = change;
                }

                public String getHigh() {
                    return high;
                }

                public void setHigh(String high) {
                    this.high = high;
                }

                public String getLow() {
                    return low;
                }

                public void setLow(String low) {
                    this.low = low;
                }

                public String getVol() {
                    return vol;
                }

                public void setVol(String vol) {
                    this.vol = vol;
                }

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class VollarUSDTBean {
                /**
                 * last : 3.00100300
                 * change : -1.2828
                 * high : 3.15800000
                 * low : 2.97090500
                 * vol : 3509.15000000
                 * time : 1564899839
                 * name : USDT
                 */

                private String last;
                private String change;
                private String high;
                private String low;
                private String vol;
                private int time;
                private String name;

                public String getLast() {
                    return last;
                }

                public void setLast(String last) {
                    this.last = last;
                }

                public String getChange() {
                    return change;
                }

                public void setChange(String change) {
                    this.change = change;
                }

                public String getHigh() {
                    return high;
                }

                public void setHigh(String high) {
                    this.high = high;
                }

                public String getLow() {
                    return low;
                }

                public void setLow(String low) {
                    this.low = low;
                }

                public String getVol() {
                    return vol;
                }

                public void setVol(String vol) {
                    this.vol = vol;
                }

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class VollarBTCBean {
                /**
                 * last : 0.00029300
                 * change : 11.8320
                 * high : 0.00029300
                 * low : 0.00026200
                 * vol : 34.10000000
                 * time : 1564899839
                 * name : BTC
                 */

                private String last;
                private String change;
                private String high;
                private String low;
                private String vol;
                private int time;
                private String name;

                public String getLast() {
                    return last;
                }

                public void setLast(String last) {
                    this.last = last;
                }

                public String getChange() {
                    return change;
                }

                public void setChange(String change) {
                    this.change = change;
                }

                public String getHigh() {
                    return high;
                }

                public void setHigh(String high) {
                    this.high = high;
                }

                public String getLow() {
                    return low;
                }

                public void setLow(String low) {
                    this.low = low;
                }

                public String getVol() {
                    return vol;
                }

                public void setVol(String vol) {
                    this.vol = vol;
                }

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class BlockBean {
            /**
             * height : 223599
             */

            private int height;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
