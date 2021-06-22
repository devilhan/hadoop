import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @Author: devilhan
 * @Date: 2021/6/22
 * @Description:
 */
public class TestHDFS {

    public Configuration conf = null;
    public FileSystem fs = null;

    @Before
    public void conn() throws IOException, InterruptedException {
        conf = new Configuration(true);
//        fs = FileSystem.get(conf);
        fs = FileSystem.get(URI.create("hdfs://tcandyj.top:9001"), conf, "hadoop");
    }

    @Test
    public void mkdir() throws IOException {
        Path dir = new Path("/devilhan");
        if (fs.exists(dir)) {
            fs.delete(dir, true);
        }
        fs.mkdirs(dir);
    }

    @Test
    public void upload() throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File("./data/hello.txt")));
        Path outfile = new Path("/wtc/new.txt");
        if (fs.exists(outfile)){
            fs.delete(outfile,true);
        }
        FSDataOutputStream output = fs.create(outfile);
        IOUtils.copyBytes(input, output, conf, true);
    }

    @Test
    public void block() throws IOException {
        Path file = new Path("/devilhan/data.txt");
        FileStatus status = fs.getFileStatus(file);
        BlockLocation[] locations = fs.getFileBlockLocations(status, 0, status.getLen());
        for (BlockLocation bl: locations){
            System.out.println(bl);
        }

        FSDataInputStream in = fs.open(file);
        System.out.println(in.readByte());
    }

    @After
    public void close() throws IOException {
        fs.close();
    }
}
