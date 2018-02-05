package com.sy.prescription.model;

public class UpdateInfo extends Basic {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {
        private int version_code;
        private int request;
        private String download_url;
        private String udpated_info;

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public int getRequest() {
            return request;
        }

        public void setRequest(int request) {
            this.request = request;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getUdpated_info() {
            return udpated_info;
        }

        public void setUdpated_info(String udpated_info) {
            this.udpated_info = udpated_info;
        }
    }
}
