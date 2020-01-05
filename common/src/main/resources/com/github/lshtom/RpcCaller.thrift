namespace java com.github.lshtom.rpc.caller

struct RpcRequest {
    1 : string code,
	2 : string msg,
	3 : binary data
}

struct RpcResponse {
    1 : string code,
    2 : string msg,
    3 : binary data
}

service RpcCaller {
	RpcResponse call(1:RpcRequest param)
}