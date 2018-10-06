/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-06  下午1:18
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadFile implements MultipartFile {

    private FileItem fileItem;
    private long size;

    public UploadFile(FileItem fileItem) {
        this.fileItem = fileItem;
        this.size = fileItem.getSize();

    }

    @Override
    public String getName() {
        return this.fileItem.getFieldName();
    }

    @Override
    public String getOriginalFilename() {
        String filename = this.fileItem.getName();
        if (filename == null) {
            return "";
        } else {
            return filename;
        }
    }

    @Override
    public String getContentType() {
        return this.fileItem.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return size == 0L;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public byte[] getBytes(){
        if (!this.isAvailable()) {
            throw new IllegalStateException("File has been moved - cannot be read again");
        } else {
            byte[] bytes = this.fileItem.get();
            return bytes != null ? bytes : new byte[0];
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!this.isAvailable()) {
            throw new IllegalStateException("File has been moved - cannot be read again");
        } else {
            InputStream inputStream = this.fileItem.getInputStream();
            return inputStream != null ? inputStream : null;
        }
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        if (!this.isAvailable()) {
            throw new IllegalStateException("File has already been moved - cannot be transferred again");
        } else if (dest.exists() && !dest.delete()) {
            throw new IOException("Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
        } else {
            try {
                this.fileItem.write(dest);
            } catch (FileUploadException e) {
                throw new IllegalStateException(e.getMessage(), e);
            } catch (IOException | IllegalStateException e1) {
                throw e1;
            } catch (Exception e2) {
                throw new IOException("File transfer failed", e2);
            }
        }
    }

    protected boolean isAvailable() {
        if (this.fileItem.isInMemory()) {
            return true;
        } else if (this.fileItem instanceof DiskFileItem) {
            return ((DiskFileItem)this.fileItem).getStoreLocation().exists();
        } else {
            return this.fileItem.getSize() == this.size;
        }
    }

}
