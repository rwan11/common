package com.rwan.im.server.example.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @author johnlog
 * @date 2018/12/25
 */
public class HttpFileServerHandler  extends SimpleChannelInboundHandler<FullHttpRequest> {


    private String url ;

    private final String INSECURE_URL = "";

    private static   String ROOT_PATH = "D:\\IdeaProjects\\im\\im\\server\\src\\main\\java\\";




    private static final Pattern ALLOW_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    public HttpFileServerHandler(String url) {
        this.url = url;
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {



        if (!request.decoderResult().isSuccess()){

            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (request.method() != HttpMethod.GET){

            sendError(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

       String uri = request.uri();
        if (uri == null){
            uri = url;
        }

        final String path = sanitizeUri(uri);
        if (path == null){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }

        final File file = new File(path);

        if (file.isHidden() || !file.exists()){
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        if (file.isDirectory()){

            if (uri.endsWith("/")){
                sendListing(ctx,file);
            }else {

                sendRedirect(ctx,uri + "/");
            }
            return;
        }
        if (!file.exists()){
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        /*RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        }catch (FileNotFoundException e){
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,fileLength);

        setContentHeader(response,file);

      *//*  if (isKeepAlive(request)){

            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }*//*

        ctx.write(response);

        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile,0,fileLength,8192), ctx.newProgressivePromise());

        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {

                System.out.println(" transfer progress:" + progress);
            }

            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println(" transfer complete");
            }
        });
       ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
       if (!isKeepAlive(request)){
           lastContentFuture.addListener(ChannelFutureListener.CLOSE);
       }*/

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");// 以只读的方式打开文件
        } catch (FileNotFoundException fnfe) {
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }
        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,fileLength);
        response.headers().add(HttpHeaderNames.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getName()));
        setContentHeader(response,file);
        if (isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);

        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());

//        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
        final RandomAccessFile finalRandomAccessFile = randomAccessFile;
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationComplete(ChannelProgressiveFuture future)
                    throws Exception {
                System.out.println("file {} transfer complete.");
//                finalRandomAccessFile.close();
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future,
                                            long progress, long total) throws Exception {
                if (total < 0) {
                    System.out.println("file {} transfer progress: {}"+ file.getName()+","+ progress);
                } else {
                    System.out.println("file {} transfer progress: {}/{}"+file.getName()+","+ progress+","+total);
                }
            }
        });
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        if(!isKeepAlive(request)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }



     /*   ChannelFuture lastContentFuture = ctx
                .writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        lastContentFuture.addListener(ChannelFutureListener.CLOSE);*/
    }

    private boolean isKeepAlive(FullHttpRequest request){


        return HttpUtil.isKeepAlive(request);
    }

    private String sanitizeUri(String uri){

        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

      /*  if (!uri.startsWith("/src")){

            return null;
        }

        if (!uri.startsWith("/")){
            return null;
        }
*/
        uri = uri.replace("/", File.separator);

        if (uri.contains(File.separator+".") || uri.contains("."+File.separator) || uri.startsWith(".") || uri.endsWith(".") ){

            return null;
        }

        return "D:\\IdeaProjects\\im\\im\\server\\src\\main\\java"+File.separator+uri;
//        return System.getProperty("user.dir")+File.separator + uri;


    }


    private static void  sendListing(ChannelHandlerContext ctx,File dir){

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=utf-8");
        String dirPath = dir.getPath();

        StringBuilder buf = new StringBuilder();
        buf.append("<!DOCTYPE>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append("目录：");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append(dirPath).append(" 目录:");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>链接:<a href=\"..\">..</a></li>");
        for (File f : dir.listFiles()){

            if (f.isHidden() || !f.canRead()){
                continue;
            }

            String name = f.getName();
            buf.append("<li>链接：<a href=\"/");

            String path = f.getPath().replace(ROOT_PATH,"");
            buf.append(path);

//            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");


        }

        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf,CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }


    private static void  sendRedirect(ChannelHandlerContext ctx,String newUri){

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION,newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }



    private void sendError(ChannelHandlerContext ctx,HttpResponseStatus status) {


        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,Unpooled.copiedBuffer("Failure: "+ status.toString(),CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    private static void setContentHeader(HttpResponse response,File file){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,mimetypesFileTypeMap.getContentType(file.getPath()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        if (ctx.channel().isActive()){
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
