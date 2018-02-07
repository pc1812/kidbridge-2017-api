package net.$51zhiyuan.development.kidbridge.module.spring;

import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * spring mvc 自定义json消息序列化
 */
public class JsonHttpMessageConverter implements HttpMessageConverter<Object> {

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();

    private Charset defaultCharset = Charset.forName("utf-8");

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz == Message.class || clazz == Map.class;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.unmodifiableList(Collections.emptyList());
    }

    @Override
    public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        Charset charset = this.getContentTypeCharset(inputMessage.getHeaders().getContentType());
        String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
        return this.kidbridgeObjectMapper.readValue(body,clazz);
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        final HttpHeaders headers = outputMessage.getHeaders();
        if (outputMessage instanceof StreamingHttpOutputMessage) {
            StreamingHttpOutputMessage streamingOutputMessage =
                    (StreamingHttpOutputMessage) outputMessage;
            streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body() {
                @Override
                public void writeTo(final OutputStream outputStream) throws IOException {
                    writeInternal(o, new HttpOutputMessage() {
                        @Override
                        public OutputStream getBody() {
                            return outputStream;
                        }
                        @Override
                        public HttpHeaders getHeaders() {
                            return headers;
                        }
                    });
                }
            });
        }
        else {
            writeInternal(o, outputMessage);
            outputMessage.getBody().flush();
        }
    }

    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException {
        Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
        outputMessage.getHeaders().setContentType(new MediaType("application","json"));
        StreamUtils.copy(this.kidbridgeObjectMapper.writeValueAsString(o), charset, outputMessage.getBody());
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        }
        else {
            return this.getDefaultCharset();
        }
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
    }
}
