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
 * 获取驾驶员列表
 * @param {string} name - 驾驶员姓名（可选）
 * @returns {Promise<Array>} 驾驶员列表
 */
export async function getDrivers(name = '') {
  const url = name ? `${BASE_URL}/driver/search?name=${encodeURIComponent(name)}` : `${BASE_URL}/driver/list`
  const response = await fetch(url)

  if (!response.ok) {
    throw new Error(`获取驾驶员列表失败: ${response.status}`)
  }

  const result = await response.json()
  return result.data || []
}

/**
 * 根据ID获取驾驶员详情
 * @param {number} id - 驾驶员ID
 * @returns {Promise<Object>} 驾驶员信息
 */
export async function getDriverById(id) {
  const response = await fetch(`${BASE_URL}/driver/${id}`)

  if (!response.ok) {
    throw new Error(`获取驾驶员详情失败: ${response.status}`)
  }

  const result = await response.json()
  return result.data
}

/**
 * 新增驾驶员
 * @param {Object} driver - 驾驶员信息
 * @returns {Promise<Object>} 响应结果
 */
export async function addDriver(driver) {
  const response = await fetch(`${BASE_URL}/driver/add`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(driver)
  })

  if (!response.ok) {
    throw new Error(`添加驾驶员失败: ${response.status}`)
  }

  return await response.json()
}

/**
 * 更新驾驶员信息
 * @param {Object} driver - 驾驶员信息
 * @returns {Promise<Object>} 响应结果
 */
export async function updateDriver(driver) {
  const response = await fetch(`${BASE_URL}/driver/update`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(driver)
  })

  if (!response.ok) {
    throw new Error(`更新驾驶员失败: ${response.status}`)
  }

  return await response.json()
}

/**
 * 删除驾驶员
 * @param {number} id - 驾驶员ID
 * @returns {Promise<Object>} 响应结果
 */
export async function deleteDriver(id) {
  const response = await fetch(`${BASE_URL}/driver/${id}`, {
    method: 'DELETE'
  })

  if (!response.ok) {
    throw new Error(`删除驾驶员失败: ${response.status}`)
  }

  return await response.json()
}

/**
 * 批量删除驾驶员
 * @param {Array<number>} ids - 驾驶员ID数组
 * @returns {Promise<Object>} 响应结果
 */
export async function batchDeleteDrivers(ids) {
  const response = await fetch(`${BASE_URL}/driver/batch`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(ids)
  })

  if (!response.ok) {
    throw new Error(`批量删除驾驶员失败: ${response.status}`)
  }

  return await response.json()
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
