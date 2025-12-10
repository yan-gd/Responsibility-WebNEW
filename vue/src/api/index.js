// API 请求模块

const BASE_URL = '/api'

/**
 * AI 聊天请求
 * @param {string} message - 用户消息
 * @param {Array} history - 历史消息记录
 * @returns {Promise<string>} AI 回复内容
 */
export async function chatWithAI(message, history = []) {
  const response = await fetch(`${BASE_URL}/ai/chat`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message,
      history
    })
  })

  if (!response.ok) {
    throw new Error(`请求失败: ${response.status}`)
  }

  const data = await response.json()
  return data
}

/**
 * AI 流式聊天请求
 * @param {string} message - 用户消息
 * @param {Array} history - 历史消息记录
 * @param {Function} onChunk - 收到数据块时的回调
 * @returns {Promise<void>}
 */
export async function chatWithAIStream(message, history = [], onChunk) {
  const response = await fetch(`${BASE_URL}/ai/chat/stream`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      message,
      history
    })
  })

  if (!response.ok) {
    throw new Error(`请求失败: ${response.status}`)
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder()

  while (true) {
    const { done, value } = await reader.read()
    if (done) break

    const chunk = decoder.decode(value, { stream: true })
    // 处理 SSE 格式的数据
    const lines = chunk.split('\n')
    for (const line of lines) {
      if (line.startsWith('data: ')) {
        const data = line.slice(6)
        if (data === '[DONE]') continue
        try {
          const parsed = JSON.parse(data)
          if (parsed.content) {
            onChunk(parsed.content)
          }
        } catch (e) {
          // 非 JSON 数据，直接传递
          if (data.trim()) {
            onChunk(data)
          }
        }
      }
    }
  }
}
