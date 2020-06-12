package com.mickey.bingimage.common;

/**
 * description
 *
 * @author mickey
 * 2020-02-14
 */
public enum HttpStatusEnum {

    Continue(100),
    Switching_Protocols(101),

    OK(200),
    Created(201),
    Accepted(202),
    Non_Authoritative_Information(203),
    No_Content(204),
    Reset_Content(205),
    Partial_Content(206),

    Multiple_Choices(300),
    Moved_Permanently(301),
    Found(302),
    See_Other(303),
    Not_Modified(304),
    Use_Proxy(305),
    Temporary_Redirect(307),

    Bad_Request(400),
    Unauthorized(401),
    Forbidden(403),
    Not_Found(404),
    Method_Not_Allowed(405),
    Not_Acceptable(406),
    Proxy_Authentication_Required(407),
    Request_Timeout(408),
    Conflict(409),
    Gone(410),
    Length_Required(411),
    Precondition_Failed(412),
    Request_Entity_Too_Large(413),
    Request_URI_Too_Long(414),
    Unsupported_Media_Type(415),
    Requested_Range_Not_Satisfiable(416),
    Expectation_Failed(417),

    Internal_Server_Error(500),
    Not_Implemented(501),
    Bad_Gateway(502),
    Service_Unavailable(503),
    Gateway_Timeout(504),
    HTTP_Version_Not_Supported(505);

    public static HttpStatusEnum valueOf(int value) {
        switch (value) {
            case 100:
                return Continue;
            case 101:
                return Switching_Protocols;

            case 200:
                return OK;
            case 201:
                return Created;
            case 202:
                return Accepted;
            case 203:
                return Non_Authoritative_Information;
            case 204:
                return No_Content;
            case 205:
                return Reset_Content;
            case 206:
                return Partial_Content;

            case 300:
                return Multiple_Choices;
            case 301:
                return Moved_Permanently;
            case 302:
                return Found;
            case 303:
                return See_Other;
            case 304:
                return Not_Modified;
            case 305:
                return Use_Proxy;
            case 307:
                return Temporary_Redirect;

            case 400:
                return Bad_Request;
            case 401:
                return Unauthorized;
            case 403:
                return Forbidden;
            case 404:
                return Not_Found;
            case 405:
                return Method_Not_Allowed;
            case 406:
                return Not_Acceptable;
            case 407:
                return Proxy_Authentication_Required;
            case 408:
                return Request_Timeout;
            case 409:
                return Conflict;
            case 410:
                return Gone;
            case 411:
                return Length_Required;
            case 412:
                return Precondition_Failed;
            case 413:
                return Request_Entity_Too_Large;
            case 414:
                return Request_URI_Too_Long;
            case 415:
                return Unsupported_Media_Type;
            case 416:
                return Requested_Range_Not_Satisfiable;
            case 417:
                return Expectation_Failed;

            case 500:
                return Internal_Server_Error;
            case 501:
                return Not_Implemented;
            case 502:
                return Bad_Gateway;
            case 503:
                return Service_Unavailable;
            case 504:
                return Gateway_Timeout;
            case 505:
                return HTTP_Version_Not_Supported;

            default:
                return null;
        }
    }

    private int value;

    HttpStatusEnum(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
