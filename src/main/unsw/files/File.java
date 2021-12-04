package unsw.files;

public class File {
    private String file_name;
    private String content;
    private String uploaded_cotent;
    private int size;
    private boolean is_uploading_not_downloading;
    private int bandwidth_rate;

    public File(String file_name, String content, boolean is_creating_file) {
        super();
        this.file_name = file_name;
        this.content = content;
        if(is_creating_file) this.uploaded_cotent = content;
        else {
            this.uploaded_cotent = "";
        }
        this.size = content.length();
    }

    public File(String file_name, String content, boolean is_creating_file, int min_bandwidth_rate) {
        super();
        this.file_name = file_name;
        this.content = content;
        if(is_creating_file) this.uploaded_cotent = content;
        else {
            this.uploaded_cotent = "";
            this.bandwidth_rate = min_bandwidth_rate;
        }
        this.size = content.length();
    }

    //methods
    /**
     * This function check if the content contains "quantum".
     */

    public boolean has_transfer_completed() {
        if (this.get_progress() < 100) return false;
        return true;
    }

	public void update_content(File file, int minutes, int rate, boolean shrink_true) {
        //rate must be byte per minute
        int new_size = uploaded_cotent.length() + rate * minutes;
        if (new_size > content.length()) {
            new_size = content.length();
            size = shrink_true ? size * 2/3 : size;
        }

        uploaded_cotent = content.substring(0, new_size);

	}

    public double get_progress() {
        System.out.println("size = " + size + " uploaded content = " + uploaded_cotent);
        return (double)uploaded_cotent.length() / size * 100;
    }

    public boolean has_quantum() {
        return content.contains("quantum");
    }

    //Getter and setters
    public int getBandwidth_rate() {
		return this.bandwidth_rate;
	}

    public String getUploaded_cotent() {
		return this.uploaded_cotent;
	}

    public boolean is_uploading_not_downloading() {
		return this.is_uploading_not_downloading;
	}

	public void setIs_uploading_not_downloading(boolean is_uploading_not_downloading) {
		this.is_uploading_not_downloading = is_uploading_not_downloading;
	}

	public String getFile_name() {
		return this.file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public int getSize() {
		return this.size;
	}

    public void setSize(int size) {
		this.size = size;
	}

}
